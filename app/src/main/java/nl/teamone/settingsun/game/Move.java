package nl.teamone.settingsun.game;

public class Move {
    protected final Block block;
    protected final Direction direction;
    protected final int distance;

    public Move(Block b, Direction dir, int dist) {
        this.block = b;
        this.direction = dir;
        this.distance = dist;
    }

    public void undo() {
        Direction reverseMove;
        switch (direction) {
            case North: reverseMove = Direction.South; break;
            case East:  reverseMove = Direction.West; break;
            case South: reverseMove = Direction.North; break;
            default:    reverseMove = Direction.South; break;
        }
        for(int i = 0; i < distance; i++)
            block.move(reverseMove);
    }
}
