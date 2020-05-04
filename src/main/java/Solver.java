import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.IntStream;

/**
 * Solves the Augmented N-Queens problem
 */
public class Solver {

    private final int mBoardSize;
    private final List<int[]> mAttackVectors;
    private final List<int[]> mScanVectors;

    Solver(final int boardSize) {
        mBoardSize = boardSize;
        mAttackVectors = List.of(new int[] {1, 1}, new int[] {1, -1});
        mScanVectors = CommonUtils.generateDiagonalVectors(boardSize);
    }

    /**
     * Represents a partial solution, used by the `solve` method to keep track of candidate solution paths.
     */
    private static class PartialSolution {
        Board board;
        int col;

        PartialSolution(Board board, int col) {
            this.board = board;
            this.col = col;
        }
    }

    /**
     * Backtracking algorithm to fill the board with queens, column-by-column, starting from the left.
     * @return optional board if solution found
     */
    public Optional<Board> solve() {
        Board board = new Board(mBoardSize);
        Stack<PartialSolution> candidates = new Stack<>();
        candidates.push(new PartialSolution(board, 0));
        while (!candidates.empty()) {
            PartialSolution currentPartialSolution = candidates.pop();
            if (currentPartialSolution.col >= mBoardSize) {
                return Optional.of(currentPartialSolution.board);
            }
            for (int rowIdx = 0; rowIdx < mBoardSize; rowIdx++) {
                if (isSafe(currentPartialSolution.board, rowIdx, currentPartialSolution.col)) {
                    candidates.push(new PartialSolution(
                            new Board(currentPartialSolution.board).placeQueen(rowIdx, currentPartialSolution.col),
                            currentPartialSolution.col + 1
                    ));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Checks if the coordinate is not being attached by other queens
     * @param row row number starts at 0
     * @param col column number starts at 0, unlike in typical chess notation
     * @return whether queen is attacking another queen
     */
    public boolean isSafe(final Board board, final int row, final int col) {
        // check horizontal lines
        List<Integer> horizontalLine = new ArrayList<>(mBoardSize);
        for (int colIdx = 0; colIdx < mBoardSize; colIdx++) {
            horizontalLine.add(CommonUtils.getRowMajor(row, colIdx, mBoardSize));
        }
        boolean isSafeFromHorizontalAttacks = board.isSafe(horizontalLine, true, row, col);
        if (!isSafeFromHorizontalAttacks) {
            return false;
        }

        // check all diagonals
        for (int[] vector : mAttackVectors) {
            if (!board.isSafe(CommonUtils.getLeftLineFromVector(vector, row, col, mBoardSize), true, row, col)) {
                return false;
            }
        }
        for (int[] vector : mScanVectors) {
            if (!board.isSafe(CommonUtils.getLeftLineFromVector(vector, row, col, mBoardSize), false, row, col)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if a board is solved. Only used for testing. Not used by the `solve` method.
     * @param board
     * @return whether a board is solved
     */
    public boolean isValid(final Board board) {
        if (board == null || !board.isFull()) {
            return false;
        }
        return IntStream.range(0, mBoardSize).allMatch(col -> isSafe(board, board.getRowFromColumn(col), col));
    }
}
