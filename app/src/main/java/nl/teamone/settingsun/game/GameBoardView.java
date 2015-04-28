package nl.teamone.settingsun.game;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.teamone.settingsun.R;

public class GameBoardView extends RelativeLayout implements View.OnTouchListener {

    private static final int GRID_WIDTH = 4, GRID_HEIGHT = 5;

    public enum Axis {
        X, Y
    }

    private boolean mBoardCreated;
    private int mTileSize;
    private RectF mGameBoardRect;
    private List<BoardTileView> mTiles = new ArrayList<>();
    private Field mField;
    private BoardTileView mMovedTile;
    private Axis mMovingOnAxis;
    private PointF mStartOffsets, mLastDragPoint;
    private Block finishBlock;

    public GameBoardView(Context context) {
        super(context);
        init(context);
    }

    public GameBoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameBoardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    /**
     * Initialise the board
     * @param context {@link android.content.Context}
     */
    private void init(Context context) {
        mField = new Field();
        finishBlock = mField.resetPositions();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!mBoardCreated) {
            determineGameboardSizes();
            fillTiles();
            mBoardCreated = true;
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        BoardTileView touchedTile = (BoardTileView) v;
        View parent = (View)((View)v.getParent()).getParent();
        TextView score = (TextView) parent.findViewById(R.id.textScore);
        if (touchedTile.isEmpty()) {
            return false;
        } else {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                // start of the gesture
                mMovedTile = touchedTile;
                mStartOffsets = new PointF(mMovedTile.getX(), mMovedTile.getY());
            } else if (event.getActionMasked() == MotionEvent.ACTION_MOVE) {
                // during the gesture
                if (mLastDragPoint != null) {
                    followFinger(event);
                }
                mLastDragPoint = new PointF(event.getRawX(), event.getRawY());
            } else if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                // end of gesture
                // if drag was over 50% or it's click, do the move
                boolean moved = checkMove();

                if(moved) {
                    mField.doMove(touchedTile.getBlock());
                    score.setText(Integer.toString(mField.getMoveCount()));
                }

                mLastDragPoint = null;
                mMovedTile = null;
                mMovingOnAxis = null;
            }
            return true;
        }
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

        // Offset to center in view
        int offsetTop = viewHeight / 2 - gameboardHeight / 2;
        int offsetLeft = viewWidth / 2 - gameboardWidth / 2;

        // Create rectangle
        mGameBoardRect = new RectF(offsetLeft, offsetTop, offsetLeft + gameboardWidth, offsetTop + gameboardHeight);
    }

    /**
     * Fills the game board
     */
    public void fillTiles() {
        removeAllViews();
        mTiles.clear();

        for(Block block : mField.getBlocks()) {
            BoardTileView boardTileView = block.getView(getContext(), mTileSize, mGameBoardRect.top, mGameBoardRect.left);
            addView(boardTileView);
            boardTileView.setOnTouchListener(this);
            mTiles.add(boardTileView);
        }
    }

    private boolean checkMove() {
        if(mMovingOnAxis == Axis.X) {
            double mod = mMovedTile.getX() % mTileSize;
            float left = (float) (mMovedTile.getX() - mod) + mGameBoardRect.left;
            if(mod < mTileSize / 2) {
                mMovedTile.setX(left);
            } else {
                mMovedTile.setX(left + mTileSize);
            }

            return mMovedTile.getX() != mStartOffsets.x;
        } else {
            double mod = mMovedTile.getY() % mTileSize;
            float top = (float) (mMovedTile.getY() - mod) + mGameBoardRect.top;
            if(mod < mTileSize / 2) {
                mMovedTile.setY(top);
            } else {
                mMovedTile.setY(top + mTileSize);
            }

            return mMovedTile.getY() != mStartOffsets.y;
        }
    }

    private void followFinger(MotionEvent event) {
        float dxEvent = event.getRawX() - mLastDragPoint.x;
        float dyEvent = event.getRawY() - mLastDragPoint.y;

        Pair<Float, Float> xy = getXYFromEvent(mMovedTile, dxEvent, dyEvent);
        // detect if this move is valid
        RectF candidateRect = new RectF(xy.first, xy.second, xy.first + mMovedTile.getWidth(), xy.second + mMovedTile.getHeight());

        boolean candidateRectInGameboard = (mGameBoardRect.contains(candidateRect));
        boolean collides = collidesWithTiles(candidateRect, mMovedTile);

        boolean impossibleMove = (!candidateRectInGameboard || collides);

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
     * @param candidateRect rectangle to check
     * @param tile tile belonging to rectangle
     * @return Whether candidateRect collides with any tilesToCheck
     */
    private boolean collidesWithTiles(RectF candidateRect, BoardTileView tile) {
        RectF otherTileRect;
        for (BoardTileView otherTile : mTiles) {
            if (!otherTile.isEmpty() && otherTile != tile) {
                otherTileRect = new RectF(otherTile.getX(), otherTile.getY(), otherTile.getX() + otherTile.getWidth(), otherTile.getY() + otherTile.getHeight());
                if (otherTileRect.intersect(candidateRect)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int undoMove() {
        mField.undoMove();
        return mField.getMoveCount();
    }

    public int resetBoard() {
        finishBlock = mField.resetPositions();
        fillTiles();
        return mField.getMoveCount();
    }

}