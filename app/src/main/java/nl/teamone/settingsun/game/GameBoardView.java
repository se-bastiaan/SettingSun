package nl.teamone.settingsun.game;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import nl.teamone.settingsun.utils.PixelUtils;

/**
 * This is the GameBoard. It generates child views for blocks and catches + processes events
 */
public class GameBoardView extends RelativeLayout implements View.OnTouchListener {

    private static final int GRID_WIDTH = 4, GRID_HEIGHT = 5;
    private static final Coordinate FINISH_COORDINATE = new Coordinate(3, 1);

    private boolean mIsBoardCreated;
    private int mTileSize;

    private BoardTileView mMovedTile;
    private Field mField;
    private Block mFinishBlock;
    private Axis mMovingOnAxis;
    private PointF mStartOffsets, mCurrentTouchLocation;
    private RectF mGameBoardRect;

    private List<BoardTileView> mTiles;
    private List<BoardListener> mBoardListeners;

    public GameBoardView(Context context) {
        super(context);
        init();
    }

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * Initialise the view and thus the field
     */
    private void init() {
        mField = new Field();
        mFinishBlock = mField.resetPositions();
        mTiles = new ArrayList<>();
        mBoardListeners = new ArrayList<>();
    }

    /**
     * Add board event listener
     * @param l {@link BoardListener}
     */
    public void addListener(BoardListener l) {
        mBoardListeners.add(l);
    }

    public void removeListener(BoardListener l) {
        mBoardListeners.remove(l);
    }

    /**
     * Default method invoked on initialisation
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mIsBoardCreated) {
            determineGameboardSizes();
            fillTiles();
            mIsBoardCreated = true;
        }
    }

    /**
     * Catch touchevents of the tiles
     * @param v {@link nl.teamone.settingsun.game.BoardTileView}
     * @param event {@link android.view.MotionEvent}
     * @return {@code true} when event was processed, {@code false} when not
     */
    public boolean onTouch(View v, MotionEvent event) {
        BoardTileView touchedTile = (BoardTileView) v;
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            // start of the gesture
            mMovedTile = touchedTile;
            mStartOffsets = new PointF(mMovedTile.getX(), mMovedTile.getY());
        } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            // during the gesture
            if (mCurrentTouchLocation != null) {
                followDrag(event);
            }
            mCurrentTouchLocation = new PointF(event.getRawX(), event.getRawY());
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
            // end of gesture
            // if drag was over 50% or it's click, do the move
            boolean moved = checkMove();

            if(moved && mMovedTile != null) {
                int row = (int) (mMovedTile.getY() / mTileSize);
                int column = (int) (mMovedTile.getX() / mTileSize);

                Block block = mMovedTile.getBlock();
                block.nextCoordinate(new Coordinate(row, column));
                mField.didMove(block);

                for(BoardListener l : mBoardListeners) {
                    l.didMove(mField.getMoveCount());
                }

                if (FINISH_COORDINATE.matches(mFinishBlock.getCoordinate())) {
                    finishGame();
                }
            }

            mCurrentTouchLocation = null;
            mMovedTile = null;
            mMovingOnAxis = null;
        }
        return true;
    }

    /**
     * Determine the size of tiles and the board
     */
    private void determineGameboardSizes() {
        // Calculate perfect sizing by looking at height/width of view
        int viewWidth = getWidth();
        int viewHeight = getHeight();
        if (viewWidth > viewHeight) {
            mTileSize = viewHeight / GRID_HEIGHT;
        } else {
            mTileSize = viewWidth / GRID_WIDTH;
        }
        int gameboardHeight = mTileSize * GRID_HEIGHT;
        int gameboardWidth = mTileSize * GRID_WIDTH;

        mGameBoardRect = new RectF(0, 0, gameboardWidth, gameboardHeight);

        setLayoutParams(new RelativeLayout.LayoutParams(gameboardWidth, gameboardHeight));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(gameboardWidth + PixelUtils.getPixelsFromDp(getContext(), 16), gameboardHeight + PixelUtils.getPixelsFromDp(getContext(), 16));
        layoutParams.leftMargin = (((View)getParent()).getWidth() - gameboardWidth - PixelUtils.getPixelsFromDp(getContext(), 16)) / 2;
        ((View)getParent()).setLayoutParams(layoutParams);
    }

    /**
     * Fills the game board
     */
    public void fillTiles() {
        removeAllViews();
        mTiles.clear();

        for(Block block : mField.getBlocks()) {
            BoardTileView boardTileView = block.getView(getContext(), mTileSize);
            addView(boardTileView);
            boardTileView.setOnTouchListener(this);
            mTiles.add(boardTileView);
        }
    }

    /**
     * Internal method to check if a move was made. If 50% of the tile size was reached, the move is
     * finished and the {@link nl.teamone.settingsun.game.BoardTileView} is place as it's supposed to
     * @return {@code true} when move is made, {@code false} when not
     */
    private boolean checkMove() {
        if(mMovedTile != null) {
            if (mMovingOnAxis == Axis.X) {
                double mod = mMovedTile.getX() % mTileSize;
                float left = (float) (mMovedTile.getX() - mod);
                if (mod < mTileSize / 2) {
                    mMovedTile.setX(left);
                } else {
                    mMovedTile.setX(left + mTileSize);
                }

                return mMovedTile.getX() != mStartOffsets.x;
            } else {
                double mod = mMovedTile.getY() % mTileSize;
                float top = (float) (mMovedTile.getY() - mod);
                if (mod < mTileSize / 2) {
                    mMovedTile.setY(top);
                } else {
                    mMovedTile.setY(top + mTileSize);
                }

                return mMovedTile.getY() != mStartOffsets.y;
            }
        }
        return false;
    }

    /**
     * Internal method to process dragging of finger on screen and move the touched tile
     * @param event {@link android.view.MotionEvent}
     */
    private void followDrag(MotionEvent event) {
        float dxEvent = event.getRawX() - mCurrentTouchLocation.x;
        float dyEvent = event.getRawY() - mCurrentTouchLocation.y;

        if(mMovedTile == null) return;

        Pair<Float, Float> xy = getXYFromEvent(mMovedTile, dxEvent, dyEvent);
        // detect if this move is valid
        RectF movingRect = new RectF(xy.first, xy.second, xy.first + mMovedTile.getWidth(), xy.second + mMovedTile.getHeight());

        boolean movingRectInGameboard = (mGameBoardRect.contains(movingRect));
        boolean collides = collidesWithTiles(movingRect, mMovedTile);

        boolean impossibleMove = (!movingRectInGameboard || collides);

        if (!impossibleMove) {
            xy = getXYFromEvent(mMovedTile, dxEvent, dyEvent);
            mMovedTile.setX(xy.first);
            mMovedTile.setY(xy.second);
        }
    }

    /**
     * Computes new x,y coordinates for given tile in given direction (x or y).
     *
     * @param tile {@link nl.teamone.settingsun.game.BoardTileView}
     * @param dxEvent change of x coordinate from touch gesture
     * @param dyEvent change of y coordinate from touch gesture
     * @return pair of first x coordinates, second y coordinates
     */
    private Pair<Float, Float> getXYFromEvent(BoardTileView tile, float dxEvent, float dyEvent) {
        float dxTile = 0, dyTile = 0;
        if(tile != null) {
            if ((mMovingOnAxis == null && Math.abs(dxEvent) > Math.abs(dyEvent)) || (mMovingOnAxis != null && mMovingOnAxis == Axis.X)) {
                dxTile = tile.getX() + dxEvent;
                dyTile = tile.getY();
                mMovingOnAxis = Axis.X;
            } else {
                dyTile = tile.getY() + dyEvent;
                dxTile = tile.getX();
                mMovingOnAxis = Axis.Y;
            }
        }
        return new Pair<>(dxTile, dyTile);
    }

    /**
     * @param checkingRect rectangle to check
     * @param tile {@link nl.teamone.settingsun.game.BoardTileView}
     * @return {@code true} if colliding, {@code false} if not
     */
    private boolean collidesWithTiles(RectF checkingRect, BoardTileView tile) {
        RectF otherRect;
        for (BoardTileView otherTile : mTiles) {
            if (otherTile != tile) {
                otherRect = new RectF(otherTile.getX(), otherTile.getY(), otherTile.getX() + otherTile.getWidth(), otherTile.getY() + otherTile.getHeight());
                if (otherRect.intersect(checkingRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Internal method called when goal is reached. Notifies listeners
     */
    private void finishGame() {
        for (BoardListener l : mBoardListeners) {
            l.gameFinished(mField.getMoveCount());
        }
        mTiles.clear();
    }

    /**
     * Call to undo move on board
     */
    public void undoMove() {
        if (mField.getMoveCount() > 0) {
            Block b = mField.popLastMove();
            if (b != null) {
                Coordinate coordinate = b.getPrevCoordinate();
                BoardTileView view = b.getView(getContext(), mTileSize);
                view.setX(mTileSize * coordinate.getColumn());
                view.setY(mTileSize * coordinate.getRow());
                b.setCoordinate(coordinate);
            }
        }

        for(BoardListener listener : mBoardListeners) {
            listener.undidMove(mField.getMoveCount());
        }
    }

    /**
     * Call to reset board
     */
    public void resetBoard() {
        mFinishBlock = mField.resetPositions();
        fillTiles();

        for(BoardListener listener : mBoardListeners) {
            listener.boardReset();
        }
    }

}
