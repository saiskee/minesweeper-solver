package bot;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import board.MinesweeperBoard;
import board.Tile;
import exceptions.GameLoseException;
import exceptions.GameWinException;
import exceptions.TileDiscoveredException;

public class MinesweeperBot {
    private MinesweeperBoard board;
    private int moves;
    public int width, height, mines;
    public ArrayList<Point> discovered;
    public ArrayList<Point> flags;
    public GameState state;
    private boolean diagnostics = false;
    private boolean guiDiagnostics = false;
    public ChanceMap chanceMap;
    public boolean control = false;
    public int failedAttempts = 0;
    public Queue<String[][]> printStream = new LinkedList<>();
    private Point currentMove = new Point();

    public MinesweeperBot(int width, int height, int mines) {
        board = new MinesweeperBoard(width, height, mines);
        this.width = width;
        this.height = height;
        this.mines = mines;
        moves = 0;
        chanceMap = new ChanceMap(board.getBoard());
        discovered = new ArrayList<>();
        flags = new ArrayList<>();
        state = GameState.running;
        
    }
    public MinesweeperBot() {
        board = new MinesweeperBoard("Control");
        control = true;
        /*
| 1 | | 1 | | 0 | | 1 | | * | 
+---+ +---+ +---+ +---+ +---+ 
| * | | 1 | | 1 | | 2 | | 2 | 
+---+ +---+ +---+ +---+ +---+ 
| 2 | | 3 | | 4 | | * | | 2 | 
+---+ +---+ +---+ +---+ +---+ 
| 1 | | * | | * | | * | | 2 | 
+---+ +---+ +---+ +---+ +---+ 
| 1 | | 2 | | 3 | | 2 | | 1 | 
+---+ +---+ +---+ +---+ +---+ 
         */
        this.width = 5;
        this.height = 5;
        this.mines = 6;
        moves = 0;
        chanceMap = new ChanceMap(board.getBoard());
        discovered = new ArrayList<>();
        flags = new ArrayList<>();
        state = GameState.running;
    }

    public void playGame() {
        if (moves == 0) {
            if (!control) {
            makeRandomMove();
            }else {
                makeMove(3,1);
            }
        }
        Point move = calculateMove();
        if (failedAttempts > 500) {
            while(!makeRandomMove());
            failedAttempts = 0;
        }
        if(!makeMove(move.x, move.y)) {
            failedAttempts++;
        }

    }

    public void setDiagnostics(boolean x) {
        diagnostics = x;
    }


    private Point calculateMove() {
        chanceMap.fillChanceMap(board.getBoard());
        double min = Double.MAX_VALUE;
        Point result = new Point();
        for (BotTile[] a : chanceMap.map) {
            for (BotTile tile : a) {
                if (tile.getChance()>0 && tile.getChance() < min && !tile.isFlag()) {
                    result.setLocation(tile.x, tile.y);
                    min = tile.getChance();
                }
            }

        }
        // Selects tile with lowest chance on chanceMap
        return result;
    }

    public double getChance(int x, int y) {
        return chanceMap.getBotTile(x, y).getChance();
    }
    public BotTile getBotTile(int x, int y) {
        return chanceMap.getBotTile(x, y);
    }
        

    public boolean makeMove(int x, int y) {
        if (board.isDiscovered(x, y)) {
            return false;
        }
        currentMove = new Point (x,y);
        String chance = getBotTile(x, y).getChance()*100 + "%";
        try {
            board.guess(x, y);
            discovered.add(new Point(x, y));
            chanceMap.fillChanceMap(board.getBoard());
            if (diagnostics) {
                System.out.println("Making move: (" + x + "," + y + ") " + chance );
                
                System.out.println(getBoardWithFlags(board.getBoard(), chanceMap.map));
            }
            if (guiDiagnostics) {
                printStream.add(getStringArrayBoard());
            }
        } catch (GameLoseException ge) {
            if (diagnostics) {
                System.out.println("Making move: (" + x + "," + y + ") " + chance);
                System.out.println("Bot lost!!!, Tiles Uncovered: " + moves);
                System.out.println(getBoardWithFlags(board.getBoard(), chanceMap.map));
                //System.out.println(chanceMap.toString());
                System.out.println("Amount of moves: " + moves);
            }
            if (guiDiagnostics) {
                String[][] toAdd = board.getEndGameStringArrayBoard();
                toAdd[y][x] += "losingtilexx249fs";
                printStream.add(toAdd);
            }
            state = GameState.lose;


        } catch (GameWinException gl) {
            if (diagnostics) {
                System.out.println("Making move: (" + x + "," + y + ") " + chance);
                System.out.println("Bot won!!!");
                System.out.println(getBoardWithFlags(board.getBoard(), chanceMap.map));
                System.out.println("Amount of moves to win: " + moves);
            }
            if (guiDiagnostics) {
                printStream.add(getStringArrayBoard());
            }
            state = GameState.win;
           

        } catch (TileDiscoveredException td) {
            return false;
        }

        moves++;
        return true;


    }

    public String getBoardWithFlags(int[][] board, BotTile[][] chances) {
        String result = new String();
        for (BotTile[] a : chances) {
            for (BotTile tile : a) {
                if (tile.isFlag()) {
                    result += "| ⛿ | ";
                } else if (tile.discovered) {
                    result += "| " + tile.getValue() + " | ";
                } else
                    result += "|   | ";
            }



            result += "\n";
            for (int i = 0; i < board[0].length; i++) {
                result += "+---+ ";
            }
            result += "\n";
        }
        return result;
    }
    
    public String[][] getStringArrayBoard(){
        BotTile[][] bt = chanceMap.map;
        String[][] result = new String[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                BotTile tile = bt[i][j];
                if (tile.isFlag()) {
                    result[i][j] = "⛿";
                } else if (tile.discovered) {
                    result[i][j] = Integer.toString(tile.getValue());
                } else
                    result[i][j] = " ";
                if (i == currentMove.y && j== currentMove.x) {
                    result[i][j] += "CURRENTMOVE";
                }
            } 
            }
        
        return result;
        }
    
    public GameState getState() {
        return state;
    }
    public void setGuiDiagnostics(boolean x) {
        guiDiagnostics = x;
    }
    public boolean makeRandomMove() {
        int randomx = (int) (Math.random() * width);
        int randomy = (int) (Math.random() * height);
        return makeMove(randomx, randomy);
    }

}
