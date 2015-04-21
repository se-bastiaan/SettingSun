package nl.teamone.settingsun.game;

/**
 * Created by shemels on 21-4-2015.
 */
public class Block {
    protected int sizeX;
    protected int sizeY;
    private Position pos;
    public Block(Position p, int x, int y){
        this.sizeX = x;
        this.sizeY = y;
        this.pos.X = p.getX();
        this.pos.Y = p.getX();
    }
    public boolean move(Direction d){
        int x, y;
        x = this.pos.X+d.getX();
        y = this.pos.Y+d.getY();
        return !(x > 3 || y > 4);
    }
}
