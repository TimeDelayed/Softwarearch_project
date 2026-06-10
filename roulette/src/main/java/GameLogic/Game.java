package GameLogic;
import java.math.BigDecimal;

public class Game {

    private int[] red = {1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36};
    private int[] black = {2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35};
    private int winningNumber;
    private int winningAmount;


    public GameResults play(BigDecimal betAmount, int betNumber, BetType betType) {
        winningNumber = (int) (Math.random() * 37); 
        winningAmount = 0;

        switch (betType) {
            case STRAIGHT_UP:
                if (betNumber == winningNumber) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(35)).intValue(); // Payout for straight up bet
                }
                break;
            case RED:
                if (isRed(winningNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(2)).intValue(); // Payout for red bet
                }
                break;
            case BLACK:
                if (isBlack(winningNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(2)).intValue(); // Payout for black bet
                }
                break;
            case EVEN:
                if (winningNumber != 0 && winningNumber % 2 == 0) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(2)).intValue(); // Payout for even bet
                }
                break;
            case ODD:
                if (winningNumber != 0 && winningNumber % 2 != 0) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(2)).intValue(); // Payout for odd bet
                }
                break;
            case SPLIT:
                if (isWinningSplitBet(betNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(17)).intValue();
                }
                break;
            case STREET:
                if (isWinningStreetBet(betNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(11)).intValue();
                }
                break;
            case CORNER:
                if (isWinningCornerBet(betNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(8)).intValue();
                }
                break;
            case LINE:
                if (isWinningLineBet(betNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(5)).intValue();
                }
                break;
            case DOZEN:
                if (isWinningDozenBet(betNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(3)).intValue();
                }
                break;
            case COLUMN:
                if (isWinningColumnBet(betNumber)) {
                    winningAmount = betAmount.multiply(BigDecimal.valueOf(3)).intValue();
                }
                break;
        }
        return new GameResults(winningNumber, winningAmount);
    }

    // Split encoding with one betting number:
    //  >0 : vertical split (n, n+3)
    //  <0 : horizontal split (abs(n), abs(n)+1)
    private boolean isWinningSplitBet(int betNumber) {
        if (winningNumber == 0 || betNumber == 0) {
            return false;
        }

        int first;
        int second;
        if (betNumber > 0) {
            first = betNumber;
            second = first + 3;
            if (first < 1 || first > 33) {
                return false;
            }
        } else {
            first = Math.abs(betNumber);
            second = first + 1;
            if (first < 1 || first > 35 || first % 3 == 0) {
                return false;
            }
        }

        return winningNumber == first || winningNumber == second;
    }

    // Street encoding: betNumber = row index 1..12
    private boolean isWinningStreetBet(int betNumber) {
        if (winningNumber == 0 || betNumber < 1 || betNumber > 12) {
            return false;
        }
        int start = (betNumber - 1) * 3 + 1;
        return winningNumber >= start && winningNumber <= start + 2;
    }

    // Corner encoding: betNumber = top-left number of the 2x2 block
    private boolean isWinningCornerBet(int betNumber) {
        if (winningNumber == 0 || betNumber < 1 || betNumber > 32 || betNumber % 3 == 0) {
            return false;
        }
        return winningNumber == betNumber
            || winningNumber == betNumber + 1
            || winningNumber == betNumber + 3
            || winningNumber == betNumber + 4;
    }

    // Line encoding: betNumber = first street row index 1..11
    private boolean isWinningLineBet(int betNumber) {
        if (winningNumber == 0 || betNumber < 1 || betNumber > 11) {
            return false;
        }
        int start = (betNumber - 1) * 3 + 1;
        return winningNumber >= start && winningNumber <= start + 5;
    }

    // Dozen encoding: betNumber 1->1-12, 2->13-24, 3->25-36
    private boolean isWinningDozenBet(int betNumber) {
        if (winningNumber == 0 || betNumber < 1 || betNumber > 3) {
            return false;
        }
        int min = (betNumber - 1) * 12 + 1;
        int max = min + 11;
        return winningNumber >= min && winningNumber <= max;
    }

    // Column encoding: betNumber 1..3 for roulette columns
    private boolean isWinningColumnBet(int betNumber) {
        if (winningNumber == 0 || betNumber < 1 || betNumber > 3) {
            return false;
        }
        return (winningNumber - 1) % 3 == (betNumber - 1);
    }

    private boolean isRed(int number) {
        for (int redNumber : red) {
            if (redNumber == number) {
                return true;
            }
        }
        return false;
    }

    private boolean isBlack(int number) {
        for (int blackNumber : black) {
            if (blackNumber == number) {
                return true;
            }
        }
        return false;
    }
}
