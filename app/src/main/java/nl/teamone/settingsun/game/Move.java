package nl.teamone.settingsun.game;

public class Move {
    protected final Block block;
    protected final Direction direction;
    protected final int distance;

    // A move contains the block that was moved, the direction it was moved in,
    // and finally the amount of spaces it was moved in that direction.
    // It shouldn't be possible for the distance to be higher than 2,
    // But the class can handle arbitrary distances.
    public Move(Block b, Direction dir, int dist) {
        this.block = b;
        this.direction = dir;
        this.distance = dist;
    }

    // Undo this move.
    public void undo() {
        // We reverse the direction this move was originally made in.
        Direction reverseMove;
        switch (direction) {
            case NORTH: reverseMove = Direction.SOUTH; break;
            case EAST:  reverseMove = Direction.WEST; break;
            case SOUTH: reverseMove = Direction.NORTH; break;
            default:    reverseMove = Direction.SOUTH; break;
        }
        // And then just move in that reverse direction.
        for(int i = 0; i < distance; i++)
            block.move(reverseMove);
        // We're not checking for the returned boolean here, because this move should logically never fail.
    }
}
