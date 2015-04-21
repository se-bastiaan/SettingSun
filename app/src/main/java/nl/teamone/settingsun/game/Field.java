package nl.teamone.settingsun.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Robin on 21/04/2015.
 */
public class Field {

    protected static boolean[][] fillMatrix;
    protected List<Block> blockList;
    protected Stack<Move> moves;

    public Field() {
        resetPositions();
    }

    public void resetPositions() {
        fillMatrix = new boolean[4][5];
        blockList = new ArrayList<>();
        moves = new Stack<>();
        blockList.add(new Block(new Position(0, 0), 1, 2));
        blockList.add(new Block(new Position(3, 0), 1, 2));

        blockList.add(new Block(new Position(1, 0), 2, 2));

        blockList.add(new Block(new Position(1, 2), 2, 1));

        blockList.add(new Block(new Position(0, 3), 1, 2));
        blockList.add(new Block(new Position(3, 3), 1, 2));

        blockList.add(new Block(new Position(1, 3), 1, 1));
        blockList.add(new Block(new Position(1, 4), 1, 1));
        blockList.add(new Block(new Position(2, 3), 1, 1));
        blockList.add(new Block(new Position(2, 4), 1, 1));
    }

    public int getMoveCount() {
        return moves.size();
    }

    public void undoMove() {
        if (moves.size() > 0) {
            moves.pop().undo();
        }
    }

}
