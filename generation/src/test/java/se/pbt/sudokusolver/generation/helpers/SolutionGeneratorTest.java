package se.pbt.sudokusolver.generation.helpers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import static org.junit.jupiter.api.Assertions.*;
import static se.pbt.sudokusolver.generation.constants.GenerationConstants.EMPTY_CELL;

@DisplayName("Solution Generator")
class SolutionGeneratorTest {

    @Nested
    @DisplayName("4x4 board")
    class SolutionGenerator4x4 {

        private SolutionGenerator generator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(4);
            generator = new SolutionGenerator();
        }

        @Test
        @DisplayName("returns true and fills an empty 4x4 board without empty cells")
        void returnsTrue_and_fillsEmptyBoard_size4() {
            boolean solved = generator.fillBoardWithSolution(board, 0, 0);

            assertTrue(solved, "Expected solution generator to succeed for empty 4x4 board");
            assertBoardFilledWithValuesInRange(board);
        }
    }

    @Nested
    @DisplayName("6x6 board")
    class SolutionGenerator6x6 {

        private SolutionGenerator generator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(6);
            generator = new SolutionGenerator();
        }

        @Test
        @DisplayName("returns true and fills an empty 6x6 board without empty cells")
        void returnsTrue_and_fillsEmptyBoard_size6() {
            boolean solved = generator.fillBoardWithSolution(board, 0, 0);

            assertTrue(solved, "Expected solution generator to succeed for empty 6x6 board");
            assertBoardFilledWithValuesInRange(board);
        }
    }

    @Nested
    @DisplayName("9x9 board")
    class SolutionGenerator9x9 {

        private SolutionGenerator generator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(9);
            generator = new SolutionGenerator();
        }

        @Test
        @DisplayName("returns true and fills an empty 9x9 board without empty cells")
        void returnsTrue_and_fillsEmptyBoard_size9() {
            boolean solved = generator.fillBoardWithSolution(board, 0, 0);

            assertTrue(solved, "Expected solution generator to succeed for empty 9x9 board");
            assertBoardFilledWithValuesInRange(board);
        }

        @Test
        @DisplayName("respects prefilled values when generating a solution")
        void respectsPrefilledValues_size9() {
            board.setValue(0, 0, 5);
            board.setValue(0, 1, 3);
            board.setValue(1, 0, 6);
            board.setValue(4, 4, 7);
            board.setValue(8, 8, 9);

            int value00 = board.getValueAt(0, 0);
            int value01 = board.getValueAt(0, 1);
            int value10 = board.getValueAt(1, 0);
            int value44 = board.getValueAt(4, 4);
            int value88 = board.getValueAt(8, 8);

            boolean solved = generator.fillBoardWithSolution(board, 0, 0);

            assertTrue(solved, "Expected solution generator to succeed for partially filled 9x9 board");
            assertBoardFilledWithValuesInRange(board);

            assertEquals(value00, board.getValueAt(0, 0));
            assertEquals(value01, board.getValueAt(0, 1));
            assertEquals(value10, board.getValueAt(1, 0));
            assertEquals(value44, board.getValueAt(4, 4));
            assertEquals(value88, board.getValueAt(8, 8));
        }
    }

    /**
     * Verifies that all cells in the given board are filled with values in the valid range 1..size.
     */
    private static void assertBoardFilledWithValuesInRange(SudokuBoard board) {
        int size = board.getSize();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board.getValueAt(row, col);
                assertNotEquals(EMPTY_CELL, value, "Expected all cells to be filled");
                assertTrue(value >= 1 && value <= size,
                        "Expected value to be within valid range 1.." + size);
            }
        }
    }

}
