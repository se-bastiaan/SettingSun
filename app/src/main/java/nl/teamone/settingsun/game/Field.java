package nl.teamone.settingsun.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Robin on 21/04/2015.
 */
public class Field {

    // A simple matrix so the blocks can check whether a position they want to move to is available.
    // It is static, so can simply be called by using 'Field.fillmatrix[x][y]' from anywhere else.
    protected static boolean[][] fillMatrix;

    // A list of blocks. The blocks keep their own position, this isn't decided by the Field class
    // nor does it depend on their position in the list.
    protected List<Block> blockList;

    // A stack containing all the moves up till now. If the game is in the starting position, the
    // stack will be empty.
    protected Stack<Move> moves;

    // Create a new field in the starting position.
    public Field() {
        resetPositions();
    }

    // We should call this method when we press the reset button.
    public void resetPositions() {
        // The fillMatrix should automatically fill itself with false
        fillMatrix = new boolean[4][5];
        // We create an empty blocklist, and fill it with the game blocks a few lines down.
        blockList = new ArrayList<>();
        // Also make an empty movelist.
        moves = new Stack<>();
        // The two vertical blocks top left and right.
        blockList.add(new Block(new Position(0, 0), 1, 2));
        blockList.add(new Block(new Position(3, 0), 1, 2));
        // The big block in the top center.
        blockList.add(new Block(new Position(1, 0), 2, 2));
        // The horizontal block in the center.
        blockList.add(new Block(new Position(1, 2), 2, 1));
        // The two vertical blocks bottom left and right.
        blockList.add(new Block(new Position(0, 3), 1, 2));
        blockList.add(new Block(new Position(3, 3), 1, 2));
        // And finally the four single blocks down the bottom.
        blockList.add(new Block(new Position(1, 3), 1, 1));
        blockList.add(new Block(new Position(1, 4), 1, 1));
        blockList.add(new Block(new Position(2, 3), 1, 1));
        blockList.add(new Block(new Position(2, 4), 1, 1));
    }

    public int getMoveCount() {
        // The amount of moves done is equal to the size of the stack. The starting position
        // has an empty stack that will return zero.
        return moves.size();
    }

    // This method should be called when we press the 'back' button.
    public void undoMove() {
        // Undo the move that's on the top of the stack.
        // Unless we haven't done any moves, in which case we just do nothing.
        if (moves.size() > 0) {
            moves.pop().undo(); // Ignore the returned boolean, since the reverse move should always be possible.
        }
    }

    public boolean doMove(Block b, Direction dir, int dist) {
        // We do the moves one by one as partial moves, so we need to store what we do.
        Stack<Move> tempMoves = new Stack<>();
        for (int i = 0; i < dist; i++) {
            // Try to do the move.
            if (! b.move(dir)) {
                // If it fails, we need to undo all the partial moves we've done, and return false.
                while (tempMoves.size() > 0)
                        tempMoves.pop().undo();
                return false;
            }
            // If it doesn't fail, we add the move to the temporary list and continue.
            tempMoves.push(new Move(b, dir, 1));
        }
        // All the partial moves succeeded, so we can store the entire move on the stack and return true.
        moves.push(new Move(b, dir, dist));
        return true;
    }

}
