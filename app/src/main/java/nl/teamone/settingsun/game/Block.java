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
        this.pos = new Position(p.getX(), p.getY());

        for (int px = 0; px < x; px++) {
            for (int py = 0; py < y; py++) {
                Field.fillMatrix[px+p.getX()][py+p.getY()] = true;
            }
        }

    }
    public boolean move(Direction d){
        int x, y;
        x = this.pos.X+d.getX();
        y = this.pos.Y+d.getY();
        return !(x > 3 || y > 4);
    }
}
