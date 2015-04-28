package nl.teamone.settingsun.game;

public class Coordinate {

	private  int mRow;
	private int mColumn;

	public Coordinate(int row, int column) {
		mRow = row;
		mColumn = column;
	}

    public int getRow() {
        return mRow;
    }

    public int getColumn() {
        return mColumn;
    }

    public void setRow(int r) {
        mRow = r;
    }

    public void setColumn(int c) {
        mColumn = c;
    }

	public boolean matches(Coordinate coordinate) {
		return coordinate.mRow == mRow && coordinate.mColumn == mColumn;
	}

	public boolean sharesAxisWith(Coordinate coordinate) {
		return (mRow == coordinate.mRow || mColumn == coordinate.mColumn);
	}

	public boolean isToRightOf(Coordinate coordinate) {
		return sharesAxisWith(coordinate) && (mColumn > coordinate.mColumn);
	}

	public boolean isToLeftOf(Coordinate coordinate) {
		return sharesAxisWith(coordinate) && (mColumn < coordinate.mColumn);
	}

	public boolean isAbove(Coordinate coordinate) {
		return sharesAxisWith(coordinate) && (mRow < coordinate.mRow);
	}

	public boolean isBelow(Coordinate coordinate) {
		return sharesAxisWith(coordinate) && (mRow > coordinate.mRow);
	}
	
	@Override
	public String toString() {
		return "[R: "+ mRow +" C:"+ mColumn +"]";
	}

}