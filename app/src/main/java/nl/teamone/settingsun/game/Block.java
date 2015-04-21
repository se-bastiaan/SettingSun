package nl.teamone.settingsun.game;

public class Block {
    protected int sizeX;
    protected int sizeY;
    private Position pos;
    public Block(Position p, int x, int y){
        this.sizeX = x;
        this.sizeY = y;
        this.pos = new Position(p.getX(), p.getY());

        // This is where we fill the Field.fillMatrix with 'true' on all the spaces this block occupies.
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

        // TODO: We should add a bit to this method!
        // First, we'll need to check whether the position we're moving to can contain the block.
        // For this, we can use the fillMatrix and the bounds.
        // If we can't, we'll need to return false.
        // Otherwise, we can make the move.
        // We'll first need to set the fillMatrix to -false- where we are right now.
        // After that, modify the X and Y values to move the block.
        // And finally we should set the fillMatrix -true- on our new position.
        // Then we can return true and be awesome and stuff.
    }
}
