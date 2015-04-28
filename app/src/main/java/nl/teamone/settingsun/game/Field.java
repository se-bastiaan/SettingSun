package nl.teamone.settingsun.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Field {

    // A list of blocks. The blocks keep their own position, this isn't decided by the Field class
    // nor does it depend on their position in the list.
    protected List<Block> blockList;

    // A stack containing all the moves up till now. If the game is in the starting position, the
    // stack will be empty.
    protected Stack<Block> moves;

    // Create a new field in the starting position.
    public Field() {
        resetPositions();
    }

    // We should call this method when we press the reset button.
    public void resetPositions() {
        // We create an empty blocklist, and fill it with the game blocks a few lines down.
        blockList = new ArrayList<>();
        // Also make an empty movelist.
        moves = new Stack<>();
        // The two vertical blocks top left and right.
        blockList.add(new Block(0, 0, 1, 2));
        blockList.add(new Block(0, 3, 1, 2));
        // The big block in the top center.
        blockList.add(new Block(0, 1, 2, 2));
        // The horizontal block in the center.
        blockList.add(new Block(2, 1, 2, 1));
        // The two vertical blocks bottom left and right.
        blockList.add(new Block(3, 0, 1, 2));
        blockList.add(new Block(3, 3, 1, 2));
        // And finally the four single blocks down the bottom.
        blockList.add(new Block(3, 1, 1, 1));
        blockList.add(new Block(4, 1, 1, 1));
        blockList.add(new Block(3, 2, 1, 1));
        blockList.add(new Block(4, 2, 1, 1));
    }

    // We probably need this method to allow for the UI to actually do anything at all.
    public List<Block> getBlocks() {
        return blockList;
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
            moves.pop().undo();
        }
    }

    public void doMove(Block b) {
        moves.push(b);
    }

}
