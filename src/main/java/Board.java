import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a chess-board of size n.
 */
public class Board {

    /**
     * List of queen position rows indexed by column
     */
    private final List<Integer> QUEEN_ROW_BY_COL;

    private final int SIZE;
    static final int INVALID_ROW = -1;

    Board(final int n) {
        SIZE = Math.max(n, 0);
        QUEEN_ROW_BY_COL = new ArrayList<>(Collections.nCopies(n, INVALID_ROW));
    }

    Board(final List<Integer> queenRows) {
        SIZE = queenRows != null ? queenRows.size() : 0;
        QUEEN_ROW_BY_COL = queenRows != null ? new ArrayList<>(queenRows) : new ArrayList<>();
    }

    @SuppressWarnings("CopyConstructorMissesField")
    Board(final Board board) {
        this(board.QUEEN_ROW_BY_COL);
    }

    /**
     * Returns whether a position of the board is safe
     * @param line               list of positions, represented in row-major order, of the line
     * @param isTraditionalCheck if false, needs two threats to be considered not safe
     * @param rowToIgnore        row index of a grid that will be ignored
     * @param colToIgnore        column index of a grid that will be ignored
     * @return                   whether a position is safe
     */
    boolean isSafe(final List<Integer> line, final boolean isTraditionalCheck,
                          final int rowToIgnore, final int colToIgnore) {
        if (QUEEN_ROW_BY_COL.isEmpty()) {
            return false;
        }
        int numQueens = 0;
        for (Integer rowCol : line) {
            int row = CommonUtils.getRow(rowCol, SIZE);
            int col = CommonUtils.getCol(rowCol, SIZE);
            if (row == rowToIgnore && col == colToIgnore) {
                continue;
            }
            if (!CommonUtils.isOnBoard(row, col, SIZE)) {
                continue;
            }
            if (row == QUEEN_ROW_BY_COL.get(col)) {
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
    Board placeQueen(final int row, final int col) {
        if (CommonUtils.isOnBoard(row, col, SIZE)) {
            QUEEN_ROW_BY_COL.set(col, row);
        }
        return this;
    }

    @SuppressWarnings("unused")
    void print() {
        System.out.println(QUEEN_ROW_BY_COL);
    }

    /**
     * Returns queen's row in a given column
     * @param col column index of a grid
     * @return    row index
     */
    int getRowFromColumn(final int col) {
        if (col >= 0 && col < SIZE) {
            return QUEEN_ROW_BY_COL.get(col);
        } else {
            return -1;
        }
    }

    /**
     * @return whether board is full of queens
     */
    boolean isFull() {
        if (QUEEN_ROW_BY_COL.isEmpty()) {
            return false;
        }
        return QUEEN_ROW_BY_COL.stream().noneMatch(row -> row == INVALID_ROW);
    }
}
