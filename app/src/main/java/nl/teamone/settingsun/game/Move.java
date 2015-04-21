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
            case NORTH: reverseMove = Direction.SOUTH; break;
            case EAST:  reverseMove = Direction.WEST; break;
            case SOUTH: reverseMove = Direction.NORTH; break;
            default:    reverseMove = Direction.SOUTH; break;
        }
        for(int i = 0; i < distance; i++)
            block.move(reverseMove);
    }
}
