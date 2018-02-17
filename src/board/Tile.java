package board;

public class Tile {
    private boolean mine = false;
    private int value = 0;
    public int x;
    public boolean discovered = false;
    public int y;
    public Tile(int x1, int y1) {
        x = x1;
        y = y1;
    }
    public boolean isMine() {
        return mine;
    }
    public void setMine(boolean x) {
        mine = x;
        value = -1;
    }
    public int getValue() {
        return value;
    }
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setValue(int val) {
        value = val;
    }
    @Override
    public String toString() {
        if (mine) {
            return "*";
        }
        else {
            return String.valueOf(value);
        }
    }
    
}
