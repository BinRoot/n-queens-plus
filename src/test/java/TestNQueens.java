import org.junit.Assert;
import org.junit.Test;

import java.util.*;
import java.util.stream.IntStream;

public class TestNQueens {

    /**
     * Solve with board size 4
     */
    @Test
    public void solveSize4() {
        Solver solver = new Solver(4);
        solver.solve().ifPresentOrElse(
                board -> Assert.assertTrue(solver.isValid(board)),
                Assert::fail
        );
    }

    /**
     * Solve with board size 5, but should expect no solution
     */
    @Test
    public void solveSize5() {
        Solver solver = new Solver(5);
        solver.solve().ifPresent(board -> Assert.fail());
    }

    /**
     * Solve with board size 8
     */
    @Test(timeout = 100)
    public void solveSize8() {
        Solver solver = new Solver(8);
        solver.solve().ifPresentOrElse(
                board -> Assert.assertTrue(solver.isValid(board)),
                Assert::fail
        );
    }

    /**
     * Solve with board size 20
     */
    @Test(timeout = 10000)
    public void solveSize20() {
        Solver solver = new Solver(20);
        solver.solve().ifPresentOrElse(
                board -> Assert.assertTrue(solver.isValid(board)),
                Assert::fail
        );
    }

    /**
     * Test the `solver.isValid` method on an empty board
     */
    @Test
    public void validateEmptyBoard() {
        Board board = new Board(4);
        Assert.assertFalse(new Solver(4).isValid(board));
    }

    /**
     * Test the `solver.isValid` method on incorrect boards
     */
    @Test
    public void validateIncorrectBoard() {
        Board board = new Board(List.of(0, 1, 2, 3));
        Solver solver = new Solver(4);
        Assert.assertFalse(solver.isValid(board));
        board = new Board(List.of(0, 0, 0, 0));
        Assert.assertFalse(solver.isValid(board));
        board = new Board(List.of(0, 3, 2, 1));
        Assert.assertFalse(solver.isValid(board));
    }

    /**
     * Test the `solver.isValid` method on a correct board
     */
    @Test
    public void validateCorrectBoard() {
        Board board = new Board(List.of(1, 3, 0, 2));
        Solver solver = new Solver(4);
        Assert.assertTrue(solver.isValid(board));
    }

    /**
     * Test the `board.isSafe` method on a safe line in a full board
     */
    @Test
    public void validateBoardLine() {
        Board board = new Board(Arrays.asList(1, 3, 0, 2));
        Assert.assertFalse(board.isSafe(List.of(2, 4), true, -1, -1));
    }

    /**
     * Test the `CommonUtils.generateDiagonalVectors` method on a size 8 board
     */
    @Test
    public void generateVectors() {
        List<int[]> actualVectors = CommonUtils.generateDiagonalVectors(8);
        List<int[]> expectedVectors = List.of(
                new int[] {1, 2}, new int[] {1, -2},
                new int[] {1, 3}, new int[] {1, -3},
                new int[] {1, 4}, new int[] {1, -4},
                new int[] {2, 1}, new int[] {2, -1},
                new int[] {2, 3}, new int[] {2, -3},
                new int[] {3, 1}, new int[] {3, -1},
                new int[] {3, 2}, new int[] {3, -2},
                new int[] {3, 4}, new int[] {3, -4},
                new int[] {4, 1}, new int[] {4, -1},
                new int[] {4, 3}, new int[] {4, -3});
        Assert.assertEquals(expectedVectors.size(), actualVectors.size());
        IntStream.range(0, actualVectors.size()).forEach(idx -> {
            int[] actualVector = actualVectors.get(idx);
            int[] expectedVector = expectedVectors.get(idx);
            Assert.assertEquals(expectedVector[0], actualVector[0]);
            Assert.assertEquals(expectedVector[1], actualVector[1]);
        });
    }

    @Test
    public void testGcd() {
        Assert.assertEquals(2, CommonUtils.gcd(6, 8));
        Assert.assertEquals(2, CommonUtils.gcd(8, 6));
        Assert.assertEquals(1, CommonUtils.gcd(1, 8));
        Assert.assertEquals(1, CommonUtils.gcd(5, 7));
    }
}
