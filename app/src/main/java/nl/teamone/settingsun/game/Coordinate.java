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
	
	@Override
	public String toString() {
		return "[R: "+ mRow +" C:"+ mColumn +"]";
	}

}