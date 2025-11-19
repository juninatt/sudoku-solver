package se.pbt.sudokusolver.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.validation.testutil.SudokuTestBoardFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Sudoku Validator:")
public class SudokuValidatorTest {

    @Nested
    @DisplayName("board size: 4x4 ")
    class SudokuValidator4x4Test {

        private SudokuValidator validator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(4);
            validator = new SudokuValidator(board);
        }

        @Test
        @DisplayName("Should validate a correct 4x4 Sudoku board")
        void testValidSudokuBoard() {
            int[][] validBoard = {
                    {1, 2, 3, 4},
                    {3, 4, 1, 2},
                    {2, 1, 4, 3},
                    {4, 3, 2, 1}
            };

            SudokuTestBoardFactory.fillBoard(board, validBoard);
            assertTrue(validator.validateBoard(), "The 4x4 board should be valid.");
        }

        @Test
        @DisplayName("Should detect duplicate row in 4x4 Sudoku board")
        void testInvalidSudokuBoard_DuplicateRow() {
            int[][] invalidBoard = {
                    {1, 2, 3, 4},
                    {1, 2, 3, 4}, // Duplicate row
                    {2, 1, 4, 3},
                    {4, 3, 2, 1}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The 4x4 board should be invalid due to duplicate rows.");
        }

        @Test
        @DisplayName("Should detect duplicate column in 4x4 Sudoku board")
        void testInvalidSudokuBoard_DuplicateColumn() {
            int[][] invalidBoard = {
                    {1, 2, 3, 4},
                    {3, 4, 1, 2},
                    {1, 2, 4, 3}, // Duplicate column (first column)
                    {4, 3, 2, 1}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The 4x4 board should be invalid due to duplicate columns.");
        }

        @Test
        @DisplayName("Should detect duplicate subgrid in 4x4 Sudoku board")
        void testInvalidSudokuBoard_DuplicateSubgrid() {
            int[][] invalidBoard = {
                    {1, 2, 3, 4},
                    {3, 4, 1, 2},
                    {1, 2, 4, 3}, // Duplicates in 2x2 subgrid
                    {4, 3, 2, 1}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The 4x4 board should be invalid due to duplicate subgrid.");
        }
    }


    @Nested
    @DisplayName("board size: 6x6")
    public class SudokuValidator6x6Test {

        private SudokuValidator validator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(6);
            validator = new SudokuValidator(board);
        }

        @Test
        @DisplayName("Should validate a correct 6x6 Sudoku board")
        void testValidSudokuBoard() {
            int[][] validBoard = {
                    {1, 2, 3, 4, 5, 6},
                    {4, 5, 6, 1, 2, 3},
                    {2, 3, 1, 5, 6, 4},
                    {5, 6, 4, 2, 3, 1},
                    {3, 1, 2, 6, 4, 5},
                    {6, 4, 5, 3, 1, 2}
            };

            SudokuTestBoardFactory.fillBoard(board, validBoard);
            assertTrue(validator.validateBoard(), "The 6x6 board should be valid.");
        }

        @Test
        @DisplayName("Should detect duplicate row in 6x6 Sudoku board")
        void testInvalidSudokuBoard_DuplicateRow() {
            int[][] invalidBoard = {
                    {1, 2, 3, 4, 5, 6},
                    {1, 2, 3, 4, 5, 6}, // Duplicate row
                    {2, 3, 1, 5, 6, 4},
                    {5, 6, 4, 2, 3, 1},
                    {3, 1, 2, 6, 4, 5},
                    {6, 4, 5, 3, 1, 2}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The 6x6 board should be invalid due to duplicate rows.");
        }

        @Test
        @DisplayName("Should detect duplicate column in 6x6 Sudoku board")
        void testInvalidSudokuBoard_DuplicateColumn() {
            int[][] invalidBoard = {
                    {1, 2, 3, 4, 5, 6},
                    {4, 5, 6, 1, 2, 3},
                    {1, 3, 1, 5, 6, 4}, // Duplicate column (first column)
                    {5, 6, 4, 2, 3, 1},
                    {3, 1, 2, 6, 4, 5},
                    {6, 4, 5, 3, 1, 2}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The 6x6 board should be invalid due to duplicate columns.");
        }

        @Test
        @DisplayName("Should detect duplicate subgrid in 6x6 Sudoku board")
        void testInvalidSudokuBoard_DuplicateSubgrid() {
            int[][] invalidBoard = {
                    {1, 2, 3, 4, 5, 6},
                    {4, 5, 6, 1, 2, 3},
                    {1, 2, 3, 4, 5, 6}, // Duplicates in 2x3 subgrid
                    {5, 6, 4, 2, 3, 1},
                    {3, 1, 2, 6, 4, 5},
                    {6, 4, 5, 3, 1, 2}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The 6x6 board should be invalid due to duplicate subgrid.");
        }
    }


    @Nested
    @DisplayName("board size: 9x9")
    public class SudokuValidator9x9Test {

        private SudokuValidator validator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(9);
            validator = new SudokuValidator(board);
        }



        @Test
        @DisplayName("Should validate a correct 9x9 Sudoku board")
        void testValidSudokuBoard() {
            int[][] validBoard = {
                    {5,3,4,6,7,8,9,1,2},
                    {6,7,2,1,9,5,3,4,8},
                    {1,9,8,3,4,2,5,6,7},
                    {8,5,9,7,6,1,4,2,3},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {9,6,1,5,3,7,2,8,4},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };

            SudokuTestBoardFactory.fillBoard(board, validBoard);
            assertTrue(validator.validateBoard(), "The board should be valid.");
        }

        @Test
        @DisplayName("Should detect duplicate row in 9x9 Sudoku board")
        void testInvalidSudokuBoard_DuplicateRow() {
            int[][] invalidBoard = {
                    {5,3,4,6,7,8,9,1,2},
                    {5,3,4,6,7,8,9,1,2}, // Duplicate of first row
                    {1,9,8,3,4,2,5,6,7},
                    {8,5,9,7,6,1,4,2,3},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {9,6,1,5,3,7,2,8,4},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The board should be invalid due to duplicate rows.");
        }

        @Test
        @DisplayName("Should detect duplicate column in 9x9 Sudoku board")
        void testInvalidSudokuBoard_DuplicateColumn() {
            int[][] invalidBoard = {
                    {5,3,4,6,7,8,9,1,2},
                    {6,7,2,1,9,5,3,4,8},
                    {1,9,8,3,4,2,5,6,7},
                    {8,5,9,7,6,1,4,2,3},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {5,3,4,6,7,8,9,1,2}, // Duplicate of first column
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The board should be invalid due to duplicate columns.");
        }

        @Test
        @DisplayName("Should detect duplicate subgrid in 9x9 Sudoku board")
        void testInvalidSudokuBoard_DuplicateSubgrid() {
            int[][] invalidBoard = {
                    {5,3,4,6,7,8,9,1,2},
                    {6,7,2,1,9,5,3,4,8},
                    {1,9,8,3,4,2,5,6,7},
                    {5,3,4,6,7,8,9,1,2}, // Duplicate of first 3x3-grid
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {9,6,1,5,3,7,2,8,4},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };

            SudokuTestBoardFactory.fillBoard(board, invalidBoard);
            assertFalse(validator.validateBoard(), "The board should be invalid due to duplicate subgrid.");
        }
    }
}
