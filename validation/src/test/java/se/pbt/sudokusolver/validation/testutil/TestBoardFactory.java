package se.pbt.sudokusolver.validation.testutil;

import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.stream.IntStream;

/**
 * Supplies an exact board state for tests without triggering game listeners
 * or automatic validation that runs during normal gameplay.
 * This keeps test setup free from side effects so tests can assert validity themselves.
 */
public final class TestBoardFactory {
    private TestBoardFactory() { }

    /**
     * Applies a predefined board configuration directly to the board,
     * avoiding repeated calls to setValue() which in runtime also updates UI and runs validation.
     *
     * @param board the test target board
     * @param values the board layout to test, must match board size exactly
     */
    public static void applyBoardState(SudokuBoard board, int[][] values) {
        if (values.length != board.getRowLength()) {
            throw new IllegalArgumentException("Size mismatch in test board input");
        }
        IntStream.range(0, board.getRowLength()).forEach(r ->
                IntStream.range(0, board.getRowLength()).forEach(c ->
                        board.setValue(r, c, values[r][c])
                )
        );
    }
}
