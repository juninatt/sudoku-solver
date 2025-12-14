package se.pbt.sudokusolver.generation.helpers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static se.pbt.sudokusolver.generation.constants.GenerationConstants.EMPTY_CELL;

@DisplayName("SudokuBuilderHelper")
class SudokuBuilderHelperTest {

    private static class TestHelper extends SudokuBuilderHelper {

        boolean isBoardFullProxy(int row, int size) {
            return isBoardFull(row, size);
        }

        int[] getNextCellProxy(int row, int col, int size) {
            return getNextCell(row, col, size);
        }

        boolean tryNumbersInCellProxy(SudokuBoard board, int row, int col,
                                      java.util.function.Function<Integer, Boolean> strategy) {
            return tryNumbersInCell(board, row, col, strategy);
        }
    }

    private final TestHelper helper = new TestHelper();

    @Nested
    @DisplayName("isBoardFull")
    class IsBoardFullTests {

        @Test
        @DisplayName("returns false when row index is within bounds")
        void returnsFalse_whenRowWithinBounds() {
            int size = 9;
            assertFalse(helper.isBoardFullProxy(0, size));
            assertFalse(helper.isBoardFullProxy(8, size));
        }

        @Test
        @DisplayName("returns true when row index is equal to or greater than board size")
        void returnsTrue_whenRowAtOrBeyondSize() {
            int size = 9;
            assertTrue(helper.isBoardFullProxy(9, size));
            assertTrue(helper.isBoardFullProxy(10, size));
        }
    }

    @Nested
    @DisplayName("getNextCell")
    class GetNextCellTests {

        @Test
        @DisplayName("moves to next column when not at end of row")
        void movesToNextColumn_whenNotAtEndOfRow() {
            int size = 4;

            int[] next = helper.getNextCellProxy(1, 1, size);

            assertEquals(1, next[0]);
            assertEquals(2, next[1]);
        }

        @Test
        @DisplayName("wraps to next row and first column at end of row")
        void wrapsToNextRow_whenAtEndOfRow() {
            int size = 4;

            int[] next = helper.getNextCellProxy(1, size - 1, size);

            assertEquals(2, next[0], "Expected row to advance when at end of row");
            assertEquals(0, next[1], "Expected column to wrap to first index");
        }
    }

    @Nested
    @DisplayName("tryNumbersInCell")
    class TryNumbersInCellTests {

        @Test
        @DisplayName("returns false and does not call strategy when no numbers are allowed")
        void returnsFalse_and_doesNotCallStrategy_whenNoNumbersAllowed() {
            SudokuBoard board = createBoardWithNoAllowedNumbersAt00();
            AtomicInteger calls = new AtomicInteger(0);

            boolean result = helper.tryNumbersInCellProxy(board, 0, 0, num -> {
                calls.incrementAndGet();
                return false;
            });

            assertFalse(result, "Expected tryNumbersInCell to return false when no numbers are allowed");
            assertEquals(0, calls.get(), "Strategy should not be called when no placements are allowed");
            assertEquals(EMPTY_CELL, board.getValueAt(0, 0), "Cell should remain empty when no placements are allowed");
        }

        @Test
        @DisplayName("returns true and leaves cell filled when strategy returns true for a valid number")
        void returnsTrue_and_leavesCellFilled_whenStrategyReturnsTrue() {
            SudokuBoard board = createBoardWithSingleAllowedNumberAt00();
            AtomicInteger calls = new AtomicInteger(0);

            boolean result = helper.tryNumbersInCellProxy(board, 0, 0, num -> {
                calls.incrementAndGet();
                return true;
            });

            assertTrue(result, "Expected tryNumbersInCell to return true when strategy returns true");
            assertEquals(1, calls.get(), "Strategy should be called exactly once when only one number is allowed");

            int value = board.getValueAt(0, 0);
            assertNotEquals(EMPTY_CELL, value, "Cell should remain filled when strategy returns true");
            assertTrue(value >= 1 && value <= board.getRowLength(),
                    "Filled value should be within valid range 1..size");
        }

        @Test
        @DisplayName("returns false and backtracks cell when strategy returns false for a valid number")
        void returnsFalse_and_backtracks_whenStrategyReturnsFalse() {
            SudokuBoard board = createBoardWithSingleAllowedNumberAt00();
            AtomicInteger calls = new AtomicInteger(0);

            boolean result = helper.tryNumbersInCellProxy(board, 0, 0, num -> {
                calls.incrementAndGet();
                return false;
            });

            assertFalse(result, "Expected tryNumbersInCell to return false when strategy never returns true");
            assertEquals(1, calls.get(), "Strategy should be called once when exactly one number is allowed");

            int value = board.getValueAt(0, 0);
            assertEquals(EMPTY_CELL, value, "Cell should be backtracked to EMPTY_CELL when strategy returns false");
        }
    }

    /**
     * Creates a 4x4 board where position (0,0) has no allowed numbers according to Sudoku rules.
     */
    private SudokuBoard createBoardWithNoAllowedNumbersAt00() {
        SudokuBoard board = new SudokuBoard(4);
        board.setValue(0, 0, EMPTY_CELL);
        board.setValue(0, 1, 1);
        board.setValue(0, 2, 2);
        board.setValue(0, 3, 3);
        board.setValue(1, 0, 4);
        return board;
    }

    /**
     * Creates a 4x4 board where exactly one number is allowed at position (0,0) according to Sudoku rules.
     */
    private SudokuBoard createBoardWithSingleAllowedNumberAt00() {
        SudokuBoard board = new SudokuBoard(4);
        board.setValue(0, 0, EMPTY_CELL);
        board.setValue(0, 1, 1);
        board.setValue(0, 2, 3);
        board.setValue(1, 0, 4);
        return board;
    }

}
