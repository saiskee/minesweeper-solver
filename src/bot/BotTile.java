package bot;

public class BotTile {

    public boolean isFlag = false;
    public boolean discovered;
    public int surroundingFlags;

    public int value;
    public double chance;
    public double fChance;
    public int x, y;
    public boolean finalChance;
    public int amountOfNeighbors;
    public String[] neighbors;
    public int amountOfUndiscoveredNeighbors;

    public BotTile(int x, int y, boolean discovered) {
        amountOfUndiscoveredNeighbors = 0;
        amountOfNeighbors = 0;
        this.x = x;
        this.y = y;
        this.discovered = discovered;
        value = 0;
        chance = 0;
        finalChance = false;
        surroundingFlags = 0;
        fChance = 0;


    }

    public int getSurroundingFlags() {
        return surroundingFlags;
    }

    public void incrementSurroundingFlags() {
        this.surroundingFlags += 1;
    }

    public void setNeighbors(String[] neighbors) {
        this.neighbors = neighbors;
    }

    public void discover() {
        discovered = true;
    }

    public void setValue(int x) {
        value = x;
    }

    public int getValue() {
        return value;
    }

    public double getChance() {
        if (finalChance) {
            return fChance;
        }
        return chance;
    }
    public void surroundingStatsReset() {
        amountOfUndiscoveredNeighbors = 0;
        amountOfNeighbors = 0;
        surroundingFlags = 0;
    }

    public boolean isDiscovered() {
        return discovered;
    }

    public void setChance(double x) {
        if (finalChance) {
            return;
        }
        chance = x;
    }

    public void setForClicking() {
        finalChance = true;
        fChance = 0.01;
    }

    @Override
    public String toString() {
        if (isFlag) {
            return "&nbsp;&nbsp;⛿&nbsp;&nbsp;";
        }
        if (discovered) {
            return "&nbsp;&nbsp;"+Integer.toString(value)+"&nbsp;&nbsp;";
        }
        if (chance == -1) {
            return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
        }
        return "&nbsp;"+Double.toString(Math.round(chance * 100.0) / 100.0) +" &nbsp;";
        // TODO: change output string to be flag or chance
    }

    public void flag() {
        chance = 100;
        isFlag = true;

    }

    public boolean isFlag() {
        return isFlag;
    }

}
