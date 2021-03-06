package nl.teamone.settingsun.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import nl.teamone.settingsun.R;

public class Field {

    // A list of blocks. The blocks keep their own position, this isn't decided by the Field class
    // nor does it depend on their position in the list.
    protected List<Block> blockList;

    // A stack containing all the moves up till now. If the game is in the starting position, the
    // stack will be empty.
    protected Stack<Block> moves;

    // Create a new field in the starting position.
    public Field() {
        blockList = new ArrayList<>();
        moves = new Stack<>();
    }

    // We should call this method when we press the reset button.
    public Block resetPositions() {
        // We create an empty blocklist, and fill it with the game blocks a few lines down.
        blockList = new ArrayList<>();
        // Also make an empty movelist.
        moves = new Stack<>();
        // The two vertical blocks top left and right.
        blockList.add(new Block(0, 0, 1, 2, R.drawable.block_background_red));
        blockList.add(new Block(0, 3, 1, 2, R.drawable.block_background_red));
        // The big block in the top center.
        Block finishBlock = new Block(0, 1, 2, 2, R.drawable.block_background_yellow);
        blockList.add(finishBlock);
        // The horizontal block in the center.
        blockList.add(new Block(2, 1, 2, 1, R.drawable.block_background_red));
        // The two vertical blocks bottom left and right.
        blockList.add(new Block(3, 0, 1, 2, R.drawable.block_background_red));
        blockList.add(new Block(3, 3, 1, 2, R.drawable.block_background_red));
        // And finally the four single blocks down the bottom.
        blockList.add(new Block(3, 1, 1, 1, R.drawable.block_background_blue));
        blockList.add(new Block(4, 1, 1, 1, R.drawable.block_background_blue));
        blockList.add(new Block(3, 2, 1, 1, R.drawable.block_background_blue));
        blockList.add(new Block(4, 2, 1, 1, R.drawable.block_background_blue));
        return finishBlock;
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
    public Block popLastMove() {
        // Undo the move that's on the top of the stack.
        // Unless we haven't done any moves, in which case we just do nothing.
        if (moves.size() > 0) {
            return moves.pop();
        }
        else
            return null;
    }

    public void didMove(Block b) {
        moves.push(b);
    }

}
