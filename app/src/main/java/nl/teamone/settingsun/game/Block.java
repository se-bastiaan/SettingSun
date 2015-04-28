package nl.teamone.settingsun.game;

import android.content.Context;
import android.graphics.Rect;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a block in a matrix-like board with columns and rows.
 * The block is identified by the coordinates of the top-left corner, but it can be bigger and thus take more space.
 * That space and the other coordinates it has are calculated and also taken into account when comparing with other blocks.
 */
public class Block {

    private Coordinate mCoordinate;
    private int mWidth;
    private int mHeight;
    private Coordinate mPrevCoordinate;

    private List<Integer> mUsedRows;
    private List<Integer> mUsedColumns;

    public Block(int row, int column, int width, int height) {
        mUsedRows = new ArrayList<>(height);
        mUsedColumns = new ArrayList<>(width);

        mWidth = width;
        mHeight = height;

        mCoordinate = new Coordinate(row, column);
        setRow(row);
        setColumn(column);
    }

    public boolean matches(Block pos) {
        return mCoordinate.matches(pos.getCoordinate()) && (pos.mHeight == mHeight) && (pos.mWidth == mWidth);
    }

    public boolean sharesAxisWith(Block pos) {
        return mUsedColumns.contains(pos.getCoordinate().getColumn()) || mUsedRows.contains(pos.getCoordinate().getColumn());
    }

    public boolean isToRightOf(Block pos) {
        return mCoordinate.isToRightOf(pos.getCoordinate());
    }

    public boolean isToLeftOf(Block pos) {
        return mCoordinate.isToLeftOf(pos.getCoordinate());
    }

    public boolean isAbove(Block pos) {
        return mCoordinate.isAbove(pos.getCoordinate());
    }

    public boolean isBelow(Block pos) {
        return mCoordinate.isBelow(pos.getCoordinate());
    }

    public void setColumn(int column) {
        mCoordinate.setColumn(column);

        for(int i = 0; i < mWidth; i++) {
            if(mUsedColumns.size() <= i) {
                mUsedColumns.add(i, column + i);
            } else {
                mUsedColumns.set(i, column + i);
            }
        }
    }

    public void setRow(int row) {
        mCoordinate.setRow(row);

        for(int i = 0; i < mWidth; i++) {
            if(mUsedRows.size() <= i) {
                mUsedRows.add(i, row + i);
            } else {
                mUsedRows.set(i, row + i);
            }
        }
    }

    public void setCoordinate(Coordinate c) {
        setColumn(c.getColumn());
        setRow(c.getRow());
    }

    public Coordinate getCoordinate() {
        return mCoordinate;
    }

    public Coordinate getPrevCoordinate() {
        return mPrevCoordinate;
    }

    public boolean move(Direction d) {
        mPrevCoordinate = new Coordinate(mCoordinate.getRow(), mCoordinate.getColumn());

        setColumn(getCoordinate().getColumn() + d.getX());
        setRow(getCoordinate().getRow() + d.getY());

        return true;
    }

    public Rect generateRect(int tileSize, float gameboardTop, float gameboardLeft) {
        int offsetTop = (int) Math.floor(gameboardTop);
        int offsetLeft = (int) Math.floor(gameboardLeft);
        offsetTop = (getCoordinate().getRow() * tileSize) + offsetTop;
        offsetLeft = (getCoordinate().getColumn() * tileSize) + offsetLeft;
        return new Rect(offsetLeft, offsetTop, offsetLeft + tileSize, offsetTop + tileSize);
    }

    public BoardTileView getView(Context context, int tileSize, float gameboardTop, float gameboardLeft) {
        int offsetTop = (int) Math.floor(gameboardTop);
        int offsetLeft = (int) Math.floor(gameboardLeft);
        offsetTop = (getCoordinate().getRow() * tileSize) + offsetTop;
        offsetLeft = (getCoordinate().getColumn() * tileSize) + offsetLeft;

        BoardTileView tileView = new BoardTileView(context, this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(tileSize * mWidth, tileSize * mHeight);
        layoutParams.leftMargin = offsetLeft;
        layoutParams.topMargin = offsetTop;
        tileView.setLayoutParams(layoutParams);

        return tileView;
    }

    @Override
    public String toString() {
        return "[R: "+ getCoordinate().getRow() +" C:"+ getCoordinate().getColumn() +" H:"+ mHeight +" W:"+ mWidth +"]";
    }
    
}
