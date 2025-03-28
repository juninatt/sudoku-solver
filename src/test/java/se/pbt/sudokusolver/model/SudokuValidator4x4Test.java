package se.pbt.sudokusolver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.utils.SudokuValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.pbt.sudokusolver.utils.SudokuTestBoardFactory.fillBoard;

/**
 * Unit tests for {@link SudokuValidator}.
 * Ensures correct validation of Sudoku boards, including rows, columns, and subgrids.
 */
@DisplayName("4x4 Sudoku Board Tests")
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

        fillBoard(board, validBoard);
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

        fillBoard(board, invalidBoard);
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

        fillBoard(board, invalidBoard);
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

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 4x4 board should be invalid due to duplicate subgrid.");
    }
}



