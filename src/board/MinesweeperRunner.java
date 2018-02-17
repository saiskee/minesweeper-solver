package board;
import java.util.Scanner;
import exceptions.GameLoseException;
import exceptions.GameWinException;
import exceptions.TileDiscoveredException;

public class MinesweeperRunner {
    
    public static void main(String[] args) {
        
        MinesweeperBoard board = new MinesweeperBoard();
        System.out.println(board.toCheatString());
        while(!board.isEndGame()) {
        System.out.println(board);
        
        Scanner r = new Scanner(System.in);
        System.out.println("guess X value: ");
        int x = r.nextInt();
        System.out.println("guess Y value: ");
        int y = r.nextInt();
        try {
            board.guess(x, y);
        } catch (GameWinException | GameLoseException | TileDiscoveredException e) {
            if (e instanceof GameWinException) {
                System.out.println("You won the game!");
                System.exit(0);
            }
            else if (e instanceof GameLoseException) {
                System.err.println("You hit a mine! You lost");
                System.exit(0);
            }
            else if (e instanceof TileDiscoveredException) {
                System.err.println("Already discovered tile, try again");
            }
        }
        }
        System.out.println(board);
        System.err.println("Game Over!!");
    }
}
