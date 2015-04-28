package nl.teamone.settingsun.game;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.widget.ImageView;

import java.util.List;

public class BoardTileView extends ImageView {

	private Block mBlock;
	private boolean mEmpty;

	public BoardTileView(Context context, Block block) {
		super(context);
        mBlock = block;
        setBackgroundColor(Color.argb(255, (int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
	}

	public boolean isEmpty() {
		return mEmpty;
	}

	public void setEmpty(boolean empty) {
		mEmpty = empty;
		if (empty) {
			setBackgroundDrawable(null);
			setAlpha(0f);
		}
	}

	public boolean isInRowOrColumnOf(BoardTileView otherTile) {
        if(otherTile != null) {
            return (mBlock.sharesAxisWith(otherTile.getBlock()));
        }
        return false;
	}

    public boolean isInRowOrColumnOf(List<BoardTileView> tiles) {
        for(BoardTileView tile : tiles) {
            boolean result = isInRowOrColumnOf(tile);
            if(result) {
                return true;
            }
        }
        return false;
    }

	public boolean isToRightOf(BoardTileView tile) {
		return mBlock.isToRightOf(tile.mBlock);
	}

	public boolean isToLeftOf(BoardTileView tile) {
		return mBlock.isToLeftOf(tile.mBlock);
	}

	public boolean isAbove(BoardTileView tile) {
		return mBlock.isAbove(tile.mBlock);
	}

	public boolean isBelow(BoardTileView tile) {
		return mBlock.isBelow(tile.mBlock);
	}

    public Block getBlock() {
        return mBlock;
    }

}