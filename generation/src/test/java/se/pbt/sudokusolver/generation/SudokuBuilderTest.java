package se.pbt.sudokusolver.generation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.generation.helpers.UniquenessChecker;

import static org.junit.jupiter.api.Assertions.*;
import static se.pbt.sudokusolver.generation.constants.GenerationConstants.EMPTY_CELL;

@DisplayName("Sudoku Builder")
class SudokuBuilderTest {

    @Test
    @DisplayName("creates solution and playable board for 4x4")
    void buildSudokuPuzzle_createsSolutionAndPlayableBoard_size4() {
        int size = 4;
        double clueFraction = 0.5;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildSudokuPuzzle(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable, "Expected playable board to be returned");
        assertNotNull(solution, "Expected solution board to be stored");

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertPlayableBoardValuesInRange(playable);
    }

    @Test
    @DisplayName("keeps all cells visible when clueFraction is 1.0 for 4x4")
    void buildSudokuPuzzle_keepsAllCellsVisible_whenClueFractionIsOne_size4() {
        int size = 4;
        double clueFraction = 1.0;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildSudokuPuzzle(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable);
        assertNotNull(solution);

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertSolutionBoardFilled(playable);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                assertEquals(solution.getValueAt(row, col), playable.getValueAt(row, col),
                        "Expected playable board to match solution when clueFraction is 1.0");
            }
        }
    }

    @Test
    @DisplayName("creates solution and playable board for 6x6")
    void buildSudokuPuzzle_createsSolutionAndPlayableBoard_size6() {
        int size = 6;
        double clueFraction = 0.5;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildSudokuPuzzle(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable, "Expected playable board to be returned");
        assertNotNull(solution, "Expected solution board to be stored");

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertPlayableBoardValuesInRange(playable);
    }

    @Test
    @DisplayName("keeps all cells visible when clueFraction is 1.0 for 6x6")
    void buildSudokuPuzzle_keepsAllCellsVisible_whenClueFractionIsOne_size6() {
        int size = 6;
        double clueFraction = 1.0;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildSudokuPuzzle(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable);
        assertNotNull(solution);

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertSolutionBoardFilled(playable);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                assertEquals(solution.getValueAt(row, col), playable.getValueAt(row, col),
                        "Expected playable board to match solution when clueFraction is 1.0");
            }
        }
    }

    @Test
    @DisplayName("creates solution and playable board for 9x9")
    void buildSudokuPuzzle_createsSolutionAndPlayableBoard_size9() {
        int size = 9;
        double clueFraction = 0.5;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildSudokuPuzzle(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable, "Expected playable board to be returned");
        assertNotNull(solution, "Expected solution board to be stored");

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertPlayableBoardValuesInRange(playable);
    }

    @Test
    @DisplayName("keeps all cells visible when clueFraction is 1.0 for 9x9")
    void buildSudokuPuzzle_keepsAllCellsVisible_whenClueFractionIsOne_size9() {
        int size = 9;
        double clueFraction = 1.0;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildSudokuPuzzle(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable);
        assertNotNull(solution);

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertSolutionBoardFilled(playable);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                assertEquals(solution.getValueAt(row, col), playable.getValueAt(row, col),
                        "Expected playable board to match solution when clueFraction is 1.0");
            }
        }
    }

    /**
     * Verifies that a solution board has no empty cells and that all values are within 1..size.
     */
    private void assertSolutionBoardFilled(SudokuBoard board) {
        int size = board.getRowLength();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board.getValueAt(row, col);
                assertNotEquals(EMPTY_CELL, value, "Expected solution board to have no empty cells");
                assertTrue(value >= 1 && value <= size,
                        "Expected solution value to be within valid range 1.." + size);
            }
        }
    }

    /**
     * Verifies that all non-empty cells in a playable board have values within 1..size.
     */
    private void assertPlayableBoardValuesInRange(SudokuBoard board) {
        int size = board.getRowLength();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board.getValueAt(row, col);
                if (value != EMPTY_CELL) {
                    assertTrue(value >= 1 && value <= size,
                            "Expected playable value to be within valid range 1.." + size + " or EMPTY_CELL");
                }
            }
        }
    }
}
