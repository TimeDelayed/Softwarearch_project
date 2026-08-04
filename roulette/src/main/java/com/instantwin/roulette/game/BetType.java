package com.instantwin.roulette.game;

import java.math.BigDecimal;
import java.util.Set;

public enum BetType implements BetStrategy {

    STRAIGHT_UP {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            return betNumber == winningNumber;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(35));
        }
    },
    RED {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            return RED_NUMBERS.contains(winningNumber);
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(2));
        }
    },
    BLACK {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            return BLACK_NUMBERS.contains(winningNumber);
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(2));
        }
    },
    EVEN {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            return winningNumber != 0 && winningNumber % 2 == 0;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(2));
        }
    },
    ODD {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            return winningNumber != 0 && winningNumber % 2 != 0;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(2));
        }
    },
    SPLIT {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            if (winningNumber == 0 || betNumber == 0) return false;
            int first, second;
            if (betNumber > 0) {
                first = betNumber;
                second = first + 3;
                if (first < 1 || first > 33) return false;
            } else {
                first = Math.abs(betNumber);
                second = first + 1;
                if (first < 1 || first > 35 || first % 3 == 0) return false;
            }
            return winningNumber == first || winningNumber == second;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(17));
        }
    },
    STREET {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            if (winningNumber == 0 || betNumber < 1 || betNumber > 12) return false;
            int start = (betNumber - 1) * 3 + 1;
            return winningNumber >= start && winningNumber <= start + 2;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(11));
        }
    },
    CORNER {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            if (winningNumber == 0 || betNumber < 1 || betNumber > 32 || betNumber % 3 == 0) return false;
            return winningNumber == betNumber
                || winningNumber == betNumber + 1
                || winningNumber == betNumber + 3
                || winningNumber == betNumber + 4;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(8));
        }
    },
    LINE {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            if (winningNumber == 0 || betNumber < 1 || betNumber > 11) return false;
            int start = (betNumber - 1) * 3 + 1;
            return winningNumber >= start && winningNumber <= start + 5;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(5));
        }
    },
    DOZEN {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            if (winningNumber == 0 || betNumber < 1 || betNumber > 3) return false;
            int min = (betNumber - 1) * 12 + 1;
            return winningNumber >= min && winningNumber <= min + 11;
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(3));
        }
    },
    COLUMN {
        @Override
        public boolean isWinner(int winningNumber, int betNumber) {
            if (winningNumber == 0 || betNumber < 1 || betNumber > 3) return false;
            return (winningNumber - 1) % 3 == (betNumber - 1);
        }
        @Override
        public BigDecimal calculatePayout(BigDecimal betAmount) {
            return betAmount.multiply(BigDecimal.valueOf(3));
        }
    };

    private static final Set<Integer> RED_NUMBERS =
        Set.of(1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36);
    private static final Set<Integer> BLACK_NUMBERS =
        Set.of(2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35);

    public abstract boolean isWinner(int winningNumber, int betNumber);
    public abstract BigDecimal calculatePayout(BigDecimal betAmount);
}
