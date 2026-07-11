package com.instantwin.roulette.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instantwin.roulette.Model.GameEntity;
import com.instantwin.roulette.Model.GameFactory;
import com.instantwin.roulette.View.GameView;
import com.instantwin.roulette.View.StatsView;
import com.instantwin.roulette.View.UserStatsView;
import com.instantwin.roulette.contract.client.IBankClient;
import com.instantwin.roulette.contract.handler.IGameHandler;
import com.instantwin.roulette.contract.view.IGameView;
import com.instantwin.roulette.contract.view.IStatsView;
import com.instantwin.roulette.contract.view.IUserStatsView;
import com.instantwin.roulette.game.BetType;
import com.instantwin.roulette.game.GameResult;
import com.instantwin.roulette.game.RouletteGame;
import com.instantwin.roulette.repostitory.IGameRepository;

/**
 * DIP: Hängt von Interfaces ab (IGameRepository, IBankClient) und Spring-Component (RouletteGame).
 * SRP: Orchestriert Spiellogik, Bank-Integration und Persistenz – delegiert beides an Spezialisten.
 */
@Service
public class GameHandler implements IGameHandler {

    private static final String RULES_TEXT =
            "Roulette Rules:\n" +
            "- The roulette wheel contains numbers 0 to 36 (37 fields total).\n" +
            "- Players place their bet before the spin by choosing a bet type and amount.\n" +
            "- The ball is spun and lands on a random number between 0 and 36.\n" +
            "- If the bet covers the winning number, the player wins according to the payout multiplier.\n" +
            "- If the bet does not cover the winning number, the player loses their bet amount.\n\n" +
            "Bet types and their rules:\n" +
            "  STRAIGHT_UP  - Bet on a single number (0–36). betNumber = chosen number.\n" +
            "  RED          - Bet on any red number. betNumber is ignored.\n" +
            "  BLACK        - Bet on any black number. betNumber is ignored.\n" +
            "  EVEN         - Bet on any even number (excluding 0). betNumber is ignored.\n" +
            "  ODD          - Bet on any odd number. betNumber is ignored.\n" +
            "  SPLIT        - Bet on two adjacent numbers. betNumber > 0: vertical split (n, n+3); betNumber < 0: horizontal split (|n|, |n|+1).\n" +
            "  STREET       - Bet on a row of three numbers. betNumber = row index (1–12).\n" +
            "  CORNER       - Bet on a block of four numbers. betNumber = top-left number of the block.\n" +
            "  LINE         - Bet on two adjacent rows (six numbers). betNumber = first row index (1–11).\n" +
            "  DOZEN        - Bet on 12 numbers: 1=1–12, 2=13–24, 3=25–36. betNumber = dozen (1–3).\n" +
            "  COLUMN       - Bet on a column of 12 numbers. betNumber = column (1–3).";

    private static final String CHANCES_TEXT =
            "Win Chances and Payout Formulas:\n\n" +
            "| Bet Type    | Numbers Covered | Win Chance | Payout Formula         |\n" +
            "|-------------|-----------------|------------|------------------------|\n" +
            "| STRAIGHT_UP | 1 / 37          |  2.70 %    | bet × 35               |\n" +
            "| SPLIT       | 2 / 37          |  5.41 %    | bet × 17               |\n" +
            "| STREET      | 3 / 37          |  8.11 %    | bet × 11               |\n" +
            "| CORNER      | 4 / 37          | 10.81 %    | bet × 8                |\n" +
            "| LINE        | 6 / 37          | 16.22 %    | bet × 5                |\n" +
            "| DOZEN       | 12 / 37         | 32.43 %    | bet × 3                |\n" +
            "| COLUMN      | 12 / 37         | 32.43 %    | bet × 3                |\n" +
            "| RED         | 18 / 37         | 48.65 %    | bet × 2                |\n" +
            "| BLACK       | 18 / 37         | 48.65 %    | bet × 2                |\n" +
            "| EVEN        | 18 / 37         | 48.65 %    | bet × 2                |\n" +
            "| ODD         | 18 / 37         | 48.65 %    | bet × 2                |\n\n" +
            "Note: 'amount' in the play response equals payout − bet_amount.\n" +
            "      Positive amount = profit; negative amount = loss.";

    private final IGameRepository gameRepository;
    private final RouletteGame rouletteGame;
    private final IBankClient bankClient;

    public GameHandler(IGameRepository gameRepository, RouletteGame rouletteGame, IBankClient bankClient) {
        this.gameRepository = gameRepository;
        this.rouletteGame = rouletteGame;
        this.bankClient = bankClient;
    }

    @Override
    public List<IGameView> findAllGames() {
        return gameRepository.findAll().stream()
                .<IGameView>map(GameView::of)
                .toList();
    }

    @Override
    public Optional<IGameView> findGameById(long id) {
        return gameRepository.findById(id).map(GameView::of);
    }

    @Override
    @Transactional
    public Optional<IGameView> deleteGame(long id) {
        return gameRepository.findById(id).map(entity -> {
            gameRepository.deleteById(entity.getId());
            return (IGameView) GameView.of(entity);
        });
    }

    /**
     * Spielablauf:
     * 1. User-Existenz via Bank-API prüfen → 404 wenn nicht gefunden
     * 2. Roulette-Runde spielen
     * 3. Bei Gewinn: Betrag per Transaktion (ROULETTE) auf Konto überweisen
     * 4. Spielergebnis in der Datenbank persistieren
     */
    @Override
    @Transactional
    public Optional<IGameView> play(long userId, BigDecimal betAmount, int betNumber, BetType betType) {
        if (!bankClient.userExists(userId)) {
            return Optional.empty();
        }

        GameResult result = rouletteGame.play(betAmount, betNumber, betType);

        if (result.payout().compareTo(BigDecimal.ZERO) > 0) {
            bankClient.createTransaction(userId, result.payout());
        }

        GameEntity entity = GameFactory.create(userId, betAmount, betNumber, betType, result);
        return Optional.of(GameView.of(gameRepository.save(entity)));
    }

    @Override
    public IStatsView getStats() {
        List<GameEntity> all = gameRepository.findAll();
        long clientCount = all.stream().mapToLong(GameEntity::getUserId).distinct().count();
        long gamesCount = all.size();
        BigDecimal totalCashOut = all.stream()
                .map(GameEntity::getPayout)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTurnover = all.stream()
                .map(GameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalProfit = totalTurnover.subtract(totalCashOut);
        return StatsView.of(clientCount, gamesCount, totalProfit, totalCashOut, totalTurnover);
    }

    @Override
    public Optional<IUserStatsView> getUserStats(long userId) {
        List<GameEntity> userGames = gameRepository.findByUserId(userId);
        if (userGames.isEmpty()) {
            return Optional.empty();
        }

        long gamesCount = userGames.size();
        BigDecimal totalTurnover = userGames.stream()
                .map(GameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalCashOut = userGames.stream()
                .map(GameEntity::getPayout)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWinnings = userGames.stream()
                .filter(e -> e.getPayout().compareTo(BigDecimal.ZERO) > 0)
                .map(e -> e.getPayout().subtract(e.getBetAmount()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalLosses = userGames.stream()
                .filter(e -> e.getPayout().compareTo(BigDecimal.ZERO) == 0)
                .map(GameEntity::getBetAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal clientProfit = totalCashOut.subtract(totalTurnover);
        BigDecimal houseProfit = totalTurnover.subtract(totalCashOut);

        return Optional.of(UserStatsView.of(
                userId, gamesCount, totalWinnings, totalLosses, clientProfit, totalTurnover, houseProfit
        ));
    }

    @Override
    public String getRules() {
        return RULES_TEXT;
    }

    @Override
    public String getChances() {
        return CHANCES_TEXT;
    }
}