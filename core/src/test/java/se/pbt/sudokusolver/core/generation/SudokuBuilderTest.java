package se.pbt.sudokusolver.core.generation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import se.pbt.sudokusolver.core.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.core.generation.helpers.UniquenessChecker;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import static org.junit.jupiter.api.Assertions.*;
import static se.pbt.sudokusolver.shared.constants.SharedConstants.EMPTY_CELL;

@DisplayName("Sudoku Builder")
class SudokuBuilderTest {

    @ParameterizedTest(name = "size={0}")
    @ValueSource(ints = {4, 6, 9})
    @DisplayName("creates solution and playable board")
    void buildPlayableBoard_createsSolutionAndPlayableBoard(int size) {
        double clueFraction = 0.5;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildPlayableBoard(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable, "Expected playable board to be returned");
        assertNotNull(solution, "Expected solution board to be stored");

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertPlayableBoardValuesInRange(playable);
    }

    @ParameterizedTest(name = "size={0}")
    @ValueSource(ints = {4, 6, 9})
    @DisplayName("keeps all cells visible when clueFraction is 1.0")
    void buildPlayableBoard_keepsAllCellsVisible_whenClueFractionIsOne(int size) {
        double clueFraction = 1.0;

        SudokuBuilder builder = new SudokuBuilder(new UniquenessChecker(), new SolutionGenerator());

        SudokuBoard playable = builder.buildPlayableBoard(size, clueFraction);
        SudokuBoard solution = builder.getSolutionBoard();

        assertNotNull(playable);
        assertNotNull(solution);

        assertEquals(size, playable.getRowLength());
        assertEquals(size, solution.getRowLength());

        assertSolutionBoardFilled(solution);
        assertSolutionBoardFilled(playable);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                assertEquals(solution.getCellValue(row, col), playable.getCellValue(row, col),
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
                int value = board.getCellValue(row, col);
                Assertions.assertNotEquals(EMPTY_CELL, value, "Expected solution board to have no empty cells");
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
                int value = board.getCellValue(row, col);
                if (value != EMPTY_CELL) {
                    assertTrue(value >= 1 && value <= size,
                            "Expected playable value to be within valid range 1.." + size + " or EMPTY_CELL");
                }
            }
        }
    }
}