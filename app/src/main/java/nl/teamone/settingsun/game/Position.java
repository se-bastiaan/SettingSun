package nl.teamone.settingsun.game;

/**
 * Created by shemels on 21-4-2015.
 */
public class Position {
    protected int X;
    protected int Y;
    public Position(int x, int y){
        this.X = x;
        this.Y = y;
    }

    public void setX(int x) {
        X = x;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    @Override
    public String toString() {
        return "("+X+", "+Y+")";
    }


}
