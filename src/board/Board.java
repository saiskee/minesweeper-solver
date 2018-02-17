package board;
import java.awt.Point;

public interface Board {
    
    public Tile[] getNeighbors(int x, int y);
    public Tile getTile(int x, int y);
}
