package board;

import java.awt.Point;
import exceptions.GameLoseException;
import exceptions.GameWinException;
import exceptions.TileDiscoveredException;


public class MinesweeperBoard implements Board {
    private Tile[][] board;
    private int mines;
    private boolean gameOver = false;
    private int nonMines;
    private int moves = 0;
    private int tilesDiscovered;
    private int height,width;


    public MinesweeperBoard(int xWidth, int yHeight, int numMines) {
        this.height = yHeight;
        this.width = xWidth;
        generateBoard(xWidth, yHeight, numMines);
        mines = numMines;
        nonMines = xWidth * yHeight - numMines;
        tilesDiscovered = 0;

    }

    public MinesweeperBoard(String s) {
        this. width = 5;
        this. height = 5;
        int mines = 6;
        this.mines = mines;
        nonMines = width * height - mines;
        generateStandardBoard();

    }

    public MinesweeperBoard() {
        int width = 5;
        int height = 5;
        int mines = 2;
        generateBoard(width, height, mines);
        nonMines = width * height - mines;
        this.mines = mines;

    }

    public Tile[] getNeighbors(int x, int y) {

        int width = board[0].length - 1;
        int height = board.length - 1;
        Tile[] neighbors = new Tile[8];
        if (x < 0 || x > width || y < 0 || y > height) {
            throw new IllegalArgumentException();
        } else if (x == width) {
            if (y == height) {
                neighbors[0] = getTile(x - 1, y - 1);
                neighbors[1] = getTile(x, y - 1);
                neighbors[3] = getTile(x - 1, y);
                return neighbors;
            }
            if (y == 0) {
                neighbors[3] = getTile(x - 1, y);
                neighbors[5] = getTile(x - 1, y + 1);
                neighbors[6] = getTile(x, y + 1);
                return neighbors;
            }
            neighbors[0] = getTile(x - 1, y - 1);
            neighbors[1] = getTile(x, y - 1);
            neighbors[3] = getTile(x - 1, y);
            neighbors[5] = getTile(x - 1, y + 1);
            neighbors[6] = getTile(x, y + 1);

        } else if (x == 0) {
            if (y == height) {
                neighbors[1] = getTile(x, y - 1);
                neighbors[2] = getTile(x + 1, y - 1);
                neighbors[4] = getTile(x + 1, y);
                return neighbors;
            } else if (y == 0) {
                neighbors[4] = getTile(x + 1, y);
                neighbors[6] = getTile(x, y + 1);
                neighbors[7] = getTile(x + 1, y + 1);
                return neighbors;

            }
            neighbors[1] = getTile(x, y - 1);
            neighbors[2] = getTile(x + 1, y - 1);
            neighbors[4] = getTile(x + 1, y);
            neighbors[6] = getTile(x, y + 1);
            neighbors[7] = getTile(x + 1, y + 1);
            return neighbors;

        } else if (y == 0) {
            neighbors[3] = getTile(x - 1, y);
            neighbors[4] = getTile(x + 1, y);
            neighbors[5] = getTile(x - 1, y + 1);
            neighbors[6] = getTile(x, y + 1);
            neighbors[7] = getTile(x + 1, y + 1);
            return neighbors;
        } else if (y == height) {
            neighbors[0] = getTile(x - 1, y - 1);
            neighbors[1] = getTile(x, y - 1);
            neighbors[2] = getTile(x + 1, y - 1);
            neighbors[4] = getTile(x + 1, y);
            neighbors[3] = getTile(x - 1, y);
            return neighbors;
        } else {

            neighbors[0] = getTile(x - 1, y - 1);
            neighbors[1] = getTile(x, y - 1);
            neighbors[2] = getTile(x + 1, y - 1);
            neighbors[3] = getTile(x - 1, y);
            neighbors[4] = getTile(x + 1, y);
            neighbors[5] = getTile(x - 1, y + 1);
            neighbors[6] = getTile(x, y + 1);
            neighbors[7] = getTile(x + 1, y + 1);
        }

        return neighbors;
    }

    public void guess(int x, int y)
            throws GameWinException, GameLoseException, TileDiscoveredException {

        if (x < 0 || x > board[0].length || y < 0 || y > board.length) {
            throw new IllegalArgumentException();
        }
        if (moves == 0) {
            if (getTile(x, y).isMine()) {
                board[y][x].setMine(false);
                board[0][0].setMine(true);
                updateTileValues();
            }
        }
        moves++;
        uncover(x, y);
    }

    private void uncover(int x, int y)
            throws GameWinException, TileDiscoveredException, GameLoseException {
        if (board[y][x].discovered) {
            throw new TileDiscoveredException();
        }
        board[y][x].discovered = true;
        tilesDiscovered++;
        if (getTile(x, y).isMine()) {
            gameOver = true;
            throw new GameLoseException();
        } else if (getTile(x, y).getValue() == 0) {
            Tile[] neighbors = getNeighbors(x, y);
            for (Tile neighbor : neighbors) {
                if (neighbor != null && !neighbor.isMine() && !neighbor.discovered) {
                    guess(neighbor.x, neighbor.y);
                }
            }
            // gets uncovered if one of neighbors = 0... random chance?
            try {
                if (y != 0) {
                    if (neighbors[1].getValue() == 0 && !neighbors[1].discovered) {
                        guess(x, y - 1);
                    }
                }
                if (x != 0) {
                    if (neighbors[3].getValue() == 0 && !neighbors[3].discovered) {
                        guess(x - 1, y);
                    }
                }
                if (x != board[0].length - 1) {
                    if (neighbors[4].getValue() == 0 && !neighbors[4].discovered) {
                        guess(x + 1, y);
                    }
                }
                if (y != board.length - 1) {
                    if (neighbors[6].getValue() == 0 && !neighbors[6].discovered) {
                        guess(x, y + 1);
                    }
                }
            } catch (NullPointerException e) {
                // WOOOHOOOO UNHANDLED EXCEPTIONSSSS
            }
        }
        if (tilesDiscovered == nonMines) {
            gameOver = true;
            throw new GameWinException();
        }

    }

    @Override
    public Tile getTile(int x, int y) {
        return board[y][x];
    }

    public boolean isDiscovered(int x, int y) {
        return board[y][x].discovered;
    }

    public int getTileValue(int x, int y) {
        Tile a = board[y][x];
        if (!a.discovered) {
            return -1;
        }
        return a.getValue();

    }

    private void generateStandardBoard() {
        board = new Tile[5][5];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = new Tile(j, i);
            }
        }
        Point[] mines = new Point[6];
        mines[0] = new Point(0, 1);
        mines[1] = new Point(1, 3);
        mines[2] = new Point(2, 3);
        mines[3] = new Point(3, 3);
        mines[4] = new Point(3, 2);
        mines[5] = new Point(4, 0);
        board[1][0].setMine(true);
        board[3][1].setMine(true);
        board[3][2].setMine(true);
        board[3][3].setMine(true);
        board[2][3].setMine(true);
        board[0][4].setMine(true);
        for (int i = 0; i < mines.length; i++) {
            int minex = mines[i].x;
            int miney = mines[i].y;
            Tile current = board[miney][minex];

            board[miney][minex].setMine(true);

            for (Tile[] a : board) {
                for (Tile temp : a) {
                    if (!temp.isMine()) {
                        Tile[] neighbors = getNeighbors(temp.x, temp.y);
                        int counter = 0;
                        for (Tile z : neighbors) {
                            if (z != null) {
                                if (z.isMine()) {
                                    counter++;
                                }
                            }
                        }
                        temp.setValue(counter);
                    }
                }
            }
        }
        return;

    }

    private void generateBoard(int x, int y, int mines) {
        board = new Tile[y][x];
        if (mines > (x * y / 2)) {
            throw new IllegalArgumentException("too many mines for this size board!");
        }


        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                board[i][j] = new Tile(j, i);
            }
        }

        // for (mines)
        // put then in random x, y coordinates where there are not already mines
        for (int i = 0; i < mines; i++) {
            int minex = (int) (Math.random() * x);
            int miney = (int) (Math.random() * y);
            Tile current = board[miney][minex];
            if (!current.isMine()) {
                board[miney][minex].setMine(true);
            } else {
                i--;
            }
        }
        updateTileValues();

        return;
    }

    private void updateTileValues() {
        for (Tile[] a : board) {
            for (Tile temp : a) {
                if (!temp.isMine() && temp.x != 0 && temp.y != 0) {
                    Tile[] neighbors = getNeighbors(temp.x, temp.y);
                    int counter = 0;
                    for (Tile z : neighbors) {
                        if (z != null) {
                            if (z.isMine()) {
                                counter++;
                            }
                        }
                    }
                    temp.setValue(counter);
                }
            }
        }

    }

    public String toCheatString() {
        String result = new String();
        for (Tile[] a : board) {
            for (Tile tile : a) {

                if (tile.isMine()) {
                    result += "| * | ";
                } else {

                    result += "| " + tile.getValue() + " | ";


                }


            }
            result += "\n";
            for (int i = 0; i < board[0].length; i++) {
                result += "+---+ ";
            }
            result += "\n";
        }
        return result;
    }

    public String[][] getEndGameStringArrayBoard() {
        String[][] result = new String[height][width];
        if (gameOver) {
        
        for (int i = 0; i < height; i++) {
            for (int j = 0;j < width; j++) {
                Tile tile = board[i][j];
                if (tile.isMine()) {
                    result[i][j] = "💥";
                }
                else {
                    if (tile.discovered) {
                        result[i][j] = Integer.toString(tile.getValue());
                    }
                    else {
                        result[i][j] = " ";
                    }
                }
            }
        }
        }
        return result;
    }

    @Override
    public String toString() {
        String result = new String();
        for (Tile[] a : board) {
            for (Tile tile : a) {
                if (gameOver) {
                    if (tile.isMine()) {
                        result += "| * | ";
                    } else {
                        if (tile.discovered) {
                            result += "| " + tile.getValue() + " | ";
                        } else
                            result += "|   | ";
                    }

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

    public int[][] getBoard() {
        int[][] result = new int[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j].discovered) {
                    result[i][j] = board[i][j].getValue();
                } else {
                    result[i][j] = -1;
                }
            }
        }
        return result;
    }

    public boolean isEndGame() {
        return gameOver;
    }

    public static void main(String args[]) {
        Integer a = new Integer(1);
        Integer b = new Integer(1);
        System.out.println(a==b);
    }



}


