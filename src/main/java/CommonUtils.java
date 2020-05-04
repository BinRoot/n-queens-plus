import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Mathematical general purpose helper functions used by Solver.
 * Not to be instantiated.
 */
public class CommonUtils {

    private CommonUtils() {}

    /**
     * Computes greatest common divisor of two integers
     * @param a an integer
     * @param b an integer
     * @return  the greatest common divisor of a and b
     */
    public static int gcd(int a, int b) {
        while (b != 0) {
            int t = a;
            a = b;
            b = t % b;
        }
        return a;
    }

    /**
     * Generates all diagonal lines except the two trivial ones for an nxn sized board
     * @param n size of the board
     * @return  list of [dx, dy] pairs representing the slope of the line. dx and dy will be always be co-prime.
     */
    public static ArrayList<int[]> generateDiagonalVectors(final int n) {
        ArrayList<int[]> vectors = new ArrayList<>();
        IntStream.range(1, 1 + n / 2).forEach(dx -> {
            IntStream.range(1, 1 + n / 2).forEach(dy -> {
                if (CommonUtils.gcd(dx, dy) == 1 && !(dx == 1 && dy == 1)) {
                    vectors.add(new int[]{dx, dy});
                    vectors.add(new int[]{dx, -dy});
                }
            });
        });
        return vectors;
    }

    /**
     *
     * @param rowCol index of a grid in row-major order
     * @param size   size of the row
     * @return       row index
     */
    public static int getRow(final int rowCol, final int size) {
        return rowCol / size;
    }

    /**
     *
     * @param rowCol index of a grid in row-major order
     * @param size   size of the row
     * @return       column index
     */
    public static int getCol(final int rowCol, final int size) {
        return rowCol % size;
    }

    /**
     *
     * @param row  row index of a grid
     * @param col  column index of a grid
     * @param size size of the row
     * @return     index of a grid in row-major order
     */
    public static int getRowMajor(final int row, final int col, final int size) {
        if (row >= size || col >= size || row < 0 || col < 0) {
            return -1;
        }
        return row * size + col;
    }

    /**
     * Returns the index after moving a piece by a vector [dx, dy]
     * @param rowCol index of a grid in row-major order
     * @param dy     change in y axis (row)
     * @param dx     change in x axis (column)
     * @param size   size of the row
     * @return       index of a grid in row-major order, after being moved
     */
    public static int addVector(int rowCol, int dx, int dy, int size) {
        return getRowMajor(getRow(rowCol, size) + dy, getCol(rowCol, size) + dx, size);
    }

    /**
     *
     * @param row  row index of a grid
     * @param col  column index of a grid
     * @param size size of the row
     * @return     whether position is on the board
     */
    public static boolean isOnBoard(int row, int col, int size) {
        return row >= 0 && row < size && col >= 0 && col < size;
    }

    /**
     * Returns a line drawn on the board by the given vector from the given position.
     * Only the items to the left of the given position are returned.
     * @param vector size 2 array of integers representing [dx, dy]
     * @param row    row index of a grid
     * @param col    column index of a grid
     * @param size   size of the row
     * @return       list of positions, represented in row-major order, of the line
     */
    public static List<Integer> getLeftLineFromVector(final int[] vector, final int row, final int col, final int size) {
        int numSquares = size * size;
        List<Integer> line = new ArrayList<>(size);
        int dx = vector[0];
        int dy = vector[1];
        int sign = -1;
        int rowCol = CommonUtils.getRowMajor(row, col, size);
        rowCol = CommonUtils.addVector(rowCol, sign * dx, sign * dy, size);
        while (rowCol >= 0 && rowCol < numSquares) {
            line.add(rowCol);
            rowCol = CommonUtils.addVector(rowCol, sign * dx, sign * dy, size);
        }
        return line;
    }
}
