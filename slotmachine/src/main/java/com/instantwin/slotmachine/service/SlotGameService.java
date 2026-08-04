package com.instantwin.slotmachine.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.instantwin.slotmachine.contract.client.ISlotRequestTransactionClient;
import com.instantwin.slotmachine.contract.model.ISlotGameFactory;
import com.instantwin.slotmachine.contract.model.ISlotGameLogic;
import com.instantwin.slotmachine.contract.service.ISlotGameService;
import com.instantwin.slotmachine.model.SlotGameEntity;
import com.instantwin.slotmachine.repository.ISlotRepository;
import com.instantwin.slotmachine.utilities.GameRulesUnavailableException;
import com.instantwin.slotmachine.utilities.SlotErrorMessages;
import com.instantwin.slotmachine.view.SlotClientStatsView;
import com.instantwin.slotmachine.view.SlotGameResultView;
import com.instantwin.slotmachine.view.SlotGameView;
import com.instantwin.slotmachine.view.SlotHouseStatsView;

@Service
public class SlotGameService implements ISlotGameService {

    private final ISlotGameLogic slotGameLogic;
    private final ISlotRepository slotGameRepository;
    private final ISlotRequestTransactionClient slotRequestTransactionClient;
    private final ISlotGameFactory slotGameFactory;
    private static final String GAME_RULES_PATH = "/gameInfo/GameRules.txt";
    private static final String GAME_CHANCES_PATH = "/gameInfo/GameChances.txt";

    public SlotGameService(
            ISlotGameLogic slotGameLogic, ISlotRepository slotGameRepository,
            ISlotRequestTransactionClient slotRequestTransactionClient, ISlotGameFactory slotGameFactory) {
        this.slotGameLogic = slotGameLogic;
        this.slotGameRepository = slotGameRepository;
        this.slotRequestTransactionClient = slotRequestTransactionClient;
        this.slotGameFactory = slotGameFactory;
    }

    @Override
    public List<SlotGameView> findAll() {
        return slotGameRepository.findAll().stream()
                .map((slotGameEntity) -> SlotGameView.of(slotGameEntity))
                .toList();
    }

    @Override
    public List<SlotGameView> findAllByUserId(long userId) {
        return slotGameRepository.findAllByUserId(userId).stream()
                .map((slotGameEntity) -> SlotGameView.of(slotGameEntity))
                .toList();
    }

    @Override
    public Optional<SlotGameView> playSlotGame(long userId, BigDecimal betAmount) {
        SlotGameResultView gameResult = slotGameLogic.placeBet(betAmount);

        BigDecimal playerNetResult = gameResult.winnings().subtract(betAmount);

        ResponseEntity<String> transactionResponse = slotRequestTransactionClient.requestTransaction(
                userId,
                playerNetResult);

        if (!transactionResponse.getStatusCode().is2xxSuccessful()) {
            return Optional.empty();
        }

        SlotGameEntity slotGameEntity = slotGameFactory.createSlotGame(
                userId,
                betAmount,
                gameResult.won(),
                playerNetResult,
                gameResult.spinResultSymbols());

        slotGameRepository.save(slotGameEntity);

        return Optional.of(SlotGameView.of(slotGameEntity));
    }

    @Override
    public Optional<SlotGameView> findById(long id) {
        return slotGameRepository.findById(id)
                .map((slotGameEntity) -> SlotGameView.of(slotGameEntity));
    }

    @Override
    public Optional<SlotGameView> deleteSlotGame(long id) {
        Optional<SlotGameEntity> slotGameEntityOptional = slotGameRepository.findById(id);
        if (slotGameEntityOptional.isPresent()) {
            SlotGameEntity slotGameEntity = slotGameEntityOptional.get();
            slotGameRepository.delete(slotGameEntity);
            return Optional.of(SlotGameView.of(slotGameEntity));
        }
        return Optional.empty();
    }

    @Override
    public String getGameRules() {
        try (var inputStream = getClass().getResourceAsStream(GAME_RULES_PATH)) {
            if (inputStream == null) {
                throw new GameRulesUnavailableException(SlotErrorMessages.GAME_RULES_FILE_ERROR,
                        new NullPointerException(SlotErrorMessages.GAME_RULES_FILE_NOT_FOUND + GAME_RULES_PATH));
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GameRulesUnavailableException(SlotErrorMessages.GAME_RULES_FILE_ERROR, e);
        }
    }

    @Override
    public String getGameChances() {
        try (var inputStream = getClass().getResourceAsStream(GAME_CHANCES_PATH)) {
            if (inputStream == null) {
                throw new GameRulesUnavailableException(SlotErrorMessages.GAME_CHANCES_FILE_ERROR,
                        new NullPointerException(SlotErrorMessages.GAME_CHANCES_FILE_NOT_FOUND + GAME_CHANCES_PATH));
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GameRulesUnavailableException(SlotErrorMessages.GAME_CHANCES_FILE_ERROR, e);
        }
    }

    @Override
    public SlotHouseStatsView getHouseStats() {
        List<SlotGameEntity> allGames = slotGameRepository.findAll();

        if (allGames.isEmpty()) {
            return new SlotHouseStatsView(
                    0,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO);
        }

        long totalClients = allGames.stream()
                .map(SlotGameEntity::getUserId)
                .distinct()
                .count();

        long totalGamesPlayed = allGames.size();

        BigDecimal totalTurnover = allGames.stream()
                .map(SlotGameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCashout = allGames.stream()
                .map(game -> game.getBetAmount().add(game.getAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalProfit = allGames.stream()
                .map(SlotGameEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .negate();

        return new SlotHouseStatsView(
                totalClients,
                totalGamesPlayed,
                totalProfit,
                totalCashout,
                totalTurnover);
    }

    @Override
    public SlotClientStatsView getUserStats(long userId) {
        List<SlotGameEntity> userGames = slotGameRepository.findAllByUserId(userId);

        if (userGames.isEmpty()) {
            return new SlotClientStatsView(
                    userId,
                    0,
                    0,
                    0,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO,
                    BigDecimal.ZERO);
        }

        long totalGamesPlayed = userGames.size();

        long totalWins = userGames.stream()
                .filter(SlotGameEntity::isWon)
                .count();

        long totalLosses = totalGamesPlayed - totalWins;

        BigDecimal totalClientProfit = userGames.stream()
                .map(SlotGameEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalHouseTurnoverFromClient = userGames.stream()
                .map(SlotGameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalHouseProfitFromClient = totalClientProfit.negate();

        return new SlotClientStatsView(
                userId,
                totalGamesPlayed,
                totalLosses,
                totalWins,
                totalClientProfit,
                totalHouseTurnoverFromClient,
                totalHouseProfitFromClient);
    }
}
