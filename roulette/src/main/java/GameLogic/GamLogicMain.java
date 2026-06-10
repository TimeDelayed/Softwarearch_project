package GameLogic;
import java.math.BigDecimal;

public class GamLogicMain {
    public static void main(String[] args) {
        Game game = new Game();
        // Example of placing a bet
        GameResults results = game.play(new BigDecimal("10"), 4, BetType.STRAIGHT_UP);
        System.out.println("Winning Number: " + results.getWinningNumber());
        System.out.println("Winning Amount: " + results.getWinningAmount());
    }
    
}
