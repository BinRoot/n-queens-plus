import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.IntStream;

/**
 * Solves the Augmented N-Queens problem
 */
public class Solver {

    private final int BOARD_SIZE;
    private final List<int[]> ATTACK_VECTORS;
    private final List<int[]> SCAN_VECTORS;

    Solver(final int boardSize) {
        BOARD_SIZE = Math.max(0, boardSize);
        ATTACK_VECTORS = List.of(new int[] {1, 1}, new int[] {1, -1});
        SCAN_VECTORS = CommonUtils.generateDiagonalVectors(boardSize);
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
        if (BOARD_SIZE <= 0) {
            return Optional.empty();
        }
        Board board = new Board(BOARD_SIZE);
        Stack<PartialSolution> candidates = new Stack<>();
        candidates.push(new PartialSolution(board, 0));
        while (!candidates.empty()) {
            PartialSolution currentPartialSolution = candidates.pop();
            if (currentPartialSolution.col >= BOARD_SIZE) {
                return Optional.of(currentPartialSolution.board);
            }
            for (int rowIdx = 0; rowIdx < BOARD_SIZE; rowIdx++) {
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
    boolean isSafe(final Board board, final int row, final int col) {
        if (board == null) {
            return false;
        }

        // check horizontal lines
        List<Integer> horizontalLine = new ArrayList<>(BOARD_SIZE);
        for (int colIdx = 0; colIdx < BOARD_SIZE; colIdx++) {
            horizontalLine.add(CommonUtils.getRowMajor(row, colIdx, BOARD_SIZE));
        }
        boolean isSafeFromHorizontalAttacks = board.isSafe(horizontalLine, true, row, col);
        if (!isSafeFromHorizontalAttacks) {
            return false;
        }

        // check all diagonals
        for (int[] vector : ATTACK_VECTORS) {
            if (!board.isSafe(CommonUtils.getLeftLineFromVector(vector, row, col, BOARD_SIZE), true, row, col)) {
                return false;
            }
        }
        for (int[] vector : SCAN_VECTORS) {
            if (!board.isSafe(CommonUtils.getLeftLineFromVector(vector, row, col, BOARD_SIZE), false, row, col)) {
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
    boolean isValid(final Board board) {
        if (board == null || !board.isFull()) {
            return false;
        }
        return IntStream.range(0, BOARD_SIZE).allMatch(col -> isSafe(board, board.getRowFromColumn(col), col));
    }
}
