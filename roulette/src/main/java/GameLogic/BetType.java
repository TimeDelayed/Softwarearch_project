package GameLogic;

public enum BetType {
    STRAIGHT_UP, // Bet on a single number
    SPLIT,       // Bet on two adjacent numbers
    STREET,      // Bet on three numbers in a row
    CORNER,      // Bet on four numbers that form a square
    LINE,        // Bet on six numbers in two adjacent rows
    DOZEN,       // Bet on 12 numbers (1-12, 13-24, 25-36)
    COLUMN,      // Bet on 12 numbers in a vertical column
    BLACK,
    RED,
    EVEN,
    ODD
}
