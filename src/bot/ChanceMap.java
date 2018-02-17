package bot;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ChanceMap {
    public BotTile[][] map;
    int height;
    int width;
    private Set<Point> flags = new HashSet<Point>();

    public ChanceMap(int[][] board) {
        height = board.length;
        width = board[0].length;
        fillChanceMap(board);


    }

    public void fillChanceMap(int[][] board) {

        BotTile[][] result = new BotTile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board[y][x] == -1) {
                    BotTile toAdd = new BotTile(x, y, false);
                    result[y][x] = toAdd;
                } else if (board[y][x] >= 0) {
                    BotTile toAdd = new BotTile(x, y, true);
                    toAdd.setValue(board[y][x]);
                    result[y][x] = toAdd;

                }
            }
        }
        map = result;
        updateChances();

    }

    public void updateChances() {
        updateTiles();
        findFlags();
        clickObviousTiles();
        findChances();
        setNeighborsArray();



    }



    private void findChances() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BotTile tile = getBotTile(x, y);
                double tileChance = tile.getChance();
                
                BotTile[] neighbors = getNeighbors(x, y);
                int value = tile.getValue();
                if (value != 0 && tile.isDiscovered()) {
                    for (BotTile neighbor : neighbors) {
                       
                        if (neighbor != null && !neighbor.isDiscovered()) {
                            
                            neighbor.setChance(neighbor.getChance() +((double) (value - tile.getSurroundingFlags())
                                    / (tile.amountOfUndiscoveredNeighbors - tile.getSurroundingFlags())));
                        }
                    }

                }
            }
        }
    }

    private void updateTiles() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BotTile tile = getBotTile(x, y);
                BotTile[] neighbors = getNeighbors(x, y);
                tile.surroundingStatsReset();
                if (tile.getValue() != 0 && tile.isDiscovered()) {
                    for (BotTile neighbor : neighbors) {
                        if (neighbor != null) {
                            tile.amountOfNeighbors++;
                            if (neighbor.isFlag()) {
                                tile.incrementSurroundingFlags();
                                
                            } 
                            if (!neighbor.isDiscovered()) {
                                tile.amountOfUndiscoveredNeighbors++;
                            }
                            
                            
                        }
                    }

                }
            }
        }


    }

    private void findFlags() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                BotTile tile = getBotTile(x, y);
                BotTile[] neighbors = getNeighbors(x, y);
                int value = tile.getValue();
                if (value != 0 && value == tile.amountOfUndiscoveredNeighbors) {
                    for (BotTile neighbor : neighbors) {
                        if (neighbor != null && !neighbor.isDiscovered() && !neighbor.isFlag()) {
                            flags.add(new Point(neighbor.x, neighbor.y));
                            neighbor.flag();
                            
                        }
                    }
                    updateTiles();
                }
            }
        }

    }

    private void clickObviousTiles() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BotTile tile = getBotTile(x, y);
                BotTile[] neighbors = getNeighbors(x, y);
                int value = tile.getValue();
                if (value != 0 && value == tile.surroundingFlags) {
                    for (BotTile neighbor : neighbors) {
                        if (neighbor!= null && !neighbor.isFlag() && !neighbor.isDiscovered()) {
                            neighbor.setForClicking();
                        }
                    }
                    updateTiles();
                }
                
            }
        }

    }

    private void setNeighborsArray() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                BotTile tile = getBotTile(x, y);
                String[] neighborsArray = new String[8];
                int counter = 0;
                for (BotTile neighbor : getNeighbors(x, y)) {
                    if (neighbor != null) {
                        neighborsArray[counter] = neighbor.toString();
                    } else {
                        neighborsArray[counter] = "null";
                    }
                    counter++;

                }
                tile.setNeighbors(neighborsArray);
            }
        }

    }

   



    public BotTile[] getNeighbors(int x, int y) {

        /*
         * 0 | 1 | 2 ---+---+--- 3 | X | 4 ---+---+--- 5 | 6 | 7
         * 
         */
        int width = this.width - 1;
        int height = this.height - 1;
        BotTile[] neighbors = new BotTile[8];
        if (x < 0 || x > width || y < 0 || y > height) {
            throw new IllegalArgumentException();
        } else if (x == width) {
            if (y == height) {
                neighbors[0] = getBotTile(x - 1, y - 1);
                neighbors[1] = getBotTile(x, y - 1);
                neighbors[3] = getBotTile(x - 1, y);
                return neighbors;
            }
            if (y == 0) {
                neighbors[3] = getBotTile(x - 1, y);
                neighbors[5] = getBotTile(x - 1, y + 1);
                neighbors[6] = getBotTile(x, y + 1);
                return neighbors;
            }
            neighbors[0] = getBotTile(x - 1, y - 1);
            neighbors[1] = getBotTile(x, y - 1);
            neighbors[3] = getBotTile(x - 1, y);
            neighbors[5] = getBotTile(x - 1, y + 1);
            neighbors[6] = getBotTile(x, y + 1);

        } else if (x == 0) {
            if (y == height) {
                neighbors[1] = getBotTile(x, y - 1);
                neighbors[2] = getBotTile(x + 1, y - 1);
                neighbors[4] = getBotTile(x + 1, y);
                return neighbors;
            } else if (y == 0) {
                neighbors[4] = getBotTile(x + 1, y);
                neighbors[6] = getBotTile(x, y + 1);
                neighbors[7] = getBotTile(x + 1, y + 1);
                return neighbors;

            }
            neighbors[1] = getBotTile(x, y - 1);
            neighbors[2] = getBotTile(x + 1, y - 1);
            neighbors[4] = getBotTile(x + 1, y);
            neighbors[6] = getBotTile(x, y + 1);
            neighbors[7] = getBotTile(x + 1, y + 1);
            return neighbors;

        } else if (y == 0) {
            neighbors[3] = getBotTile(x - 1, y);
            neighbors[4] = getBotTile(x + 1, y);
            neighbors[5] = getBotTile(x - 1, y + 1);
            neighbors[6] = getBotTile(x, y + 1);
            neighbors[7] = getBotTile(x + 1, y + 1);
            return neighbors;
        } else if (y == height) {
            neighbors[0] = getBotTile(x - 1, y - 1);
            neighbors[1] = getBotTile(x, y - 1);
            neighbors[2] = getBotTile(x + 1, y - 1);
            neighbors[4] = getBotTile(x + 1, y);
            neighbors[3] = getBotTile(x - 1, y);
            return neighbors;
        } else {

            neighbors[0] = getBotTile(x - 1, y - 1);
            neighbors[1] = getBotTile(x, y - 1);
            neighbors[2] = getBotTile(x + 1, y - 1);
            neighbors[3] = getBotTile(x - 1, y);
            neighbors[4] = getBotTile(x + 1, y);
            neighbors[5] = getBotTile(x - 1, y + 1);
            neighbors[6] = getBotTile(x, y + 1);
            neighbors[7] = getBotTile(x + 1, y + 1);
        }

        return neighbors;
    }

    public BotTile getBotTile(int x, int y) {
        return map[y][x];
    }

    @Override
    public String toString() {
        String result = new String();
        for (BotTile[] a : map) {
            for (BotTile b : a) {
                result += Math.round(b.getChance() * 100) / 100.0 + " , ";
            }
            result += "\n";
        }
        return result;
    }
}
