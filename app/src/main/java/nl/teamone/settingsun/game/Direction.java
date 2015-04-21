package nl.teamone.settingsun.game;

/**
 * Created by shemels on 21-4-2015.
 */
public enum Direction {
    North(0,-1),
    East(1,0),
    South(0,1),
    West(-1,0);

    private int x, y;

    Direction(int a, int b){
        this.x=a;
        this.y=b;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
