import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a chess-board of size n.
 */
public class Board {
    private final List<Integer> mQueenRows;
    private final int mSize;
    public static final int INVALID_ROW = -1;

    Board(final int n) {
        mSize = n;
        mQueenRows = new ArrayList<>(Collections.nCopies(n, INVALID_ROW));
    }

    Board(final List<Integer> queenRows) {
        mSize = queenRows.size();
        mQueenRows = new ArrayList<>(queenRows);
    }

    @SuppressWarnings("CopyConstructorMissesField")
    Board(final Board board) {
        this(board.mQueenRows);
    }

    /**
     * Returns whether a position of the board is safe
     * @param line               list of positions, represented in row-major order, of the line
     * @param isTraditionalCheck if false, needs two threats to be considered not safe
     * @param rowToIgnore        row index of a grid that will be ignored
     * @param colToIgnore        column index of a grid that will be ignored
     * @return                   whether a position is safe
     */
    public boolean isSafe(final List<Integer> line, final boolean isTraditionalCheck,
                          final int rowToIgnore, final int colToIgnore) {
        if (mQueenRows.isEmpty()) {
            return false;
        }
        int numQueens = 0;
        for (Integer rowCol : line) {
            int row = CommonUtils.getRow(rowCol, mSize);
            int col = CommonUtils.getCol(rowCol, mSize);
            if (row == rowToIgnore && col == colToIgnore) {
                continue;
            }
            if (!CommonUtils.isOnBoard(row, col, mSize)) {
                continue;
            }
            if (row == mQueenRows.get(col)) {
                numQueens++;
                if (isTraditionalCheck || numQueens > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Places a queen on the board
     * @param row row index of a grid
     * @param col column index of a grid
     * @return    self
     */
    public Board placeQueen(final int row, final int col) {
        if (CommonUtils.isOnBoard(row, col, mSize)) {
            mQueenRows.set(col, row);
        }
        return this;
    }

    @SuppressWarnings("unused")
    public void print() {
        System.out.println(mQueenRows);
    }

    /**
     * Returns queen's row in a given column
     * @param col column index of a grid
     * @return    row index
     */
    public int getRowFromColumn(final int col) {
        if (col >= 0 && col < mSize) {
            return mQueenRows.get(col);
        } else {
            return -1;
        }
    }

    /**
     * @return whether board is full of queens
     */
    public boolean isFull() {
        if (mQueenRows.isEmpty()) {
            return false;
        }
        return mQueenRows.stream().noneMatch(row -> row == INVALID_ROW);
    }
}
