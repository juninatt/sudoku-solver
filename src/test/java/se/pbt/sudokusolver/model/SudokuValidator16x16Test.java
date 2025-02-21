package se.pbt.sudokusolver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.models.SudokuValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.pbt.sudokusolver.utils.SudokuTestBoardFactory.fillBoard;

/**
 * Unit tests for {@link SudokuValidator} using a 16x16 board.
 * Ensures correct validation of rows, columns, and subgrids.
 */
@DisplayName("16x16 Sudoku Board Tests")
class SudokuValidator16x16Test {

    private SudokuValidator validator;
    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard(16);
        validator = new SudokuValidator(board);
    }

    @Test
    @DisplayName("Should validate a correct 16x16 Sudoku board")
    void testValidSudokuBoard() {
        fillBoard(board, validBoard());
        assertTrue(validator.validateBoard(), "The 16x16 board should be valid.");
    }

    @Test
    @DisplayName("Should detect duplicate row in 16x16 Sudoku board")
    void testInvalidSudokuBoard_DuplicateRow() {
        int[][] invalidBoard = validBoard();
        invalidBoard[1] = invalidBoard[0]; // Introduces a duplicate row

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 16x16 board should be invalid due to duplicate rows.");
    }

    @Test
    @DisplayName("Should detect duplicate column in 16x16 Sudoku board")
    void testInvalidSudokuBoard_DuplicateColumn() {
        int[][] invalidBoard = validBoard();
        for (int i = 0; i < 16; i++) {
            invalidBoard[i][1] = invalidBoard[i][0]; // Introduces a duplicate column
        }

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 16x16 board should be invalid due to duplicate columns.");
    }

    @Test
    @DisplayName("Should detect duplicate subgrid in 16x16 Sudoku board")
    void testInvalidSudokuBoard_DuplicateSubgrid() {
        int[][] invalidBoard = validBoard();
        invalidBoard[4][4] = invalidBoard[0][0]; // Introduces a duplicate in the first 4x4 subgrid

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 16x16 board should be invalid due to duplicate subgrid.");
    }

    /**
     * Returns a valid 16x16 Sudoku board for testing.
     * The board follows standard 16x16 Sudoku rules.
     */
    private int[][] validBoard() {
        return new int[][] {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16},
                {5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4},
                {9, 10, 11, 12, 13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8},
                {13, 14, 15, 16, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},

                {2, 1, 4, 3, 6, 5, 8, 7, 10, 9, 12, 11, 14, 13, 16, 15},
                {6, 5, 8, 7, 10, 9, 12, 11, 14, 13, 16, 15, 2, 1, 4, 3},
                {10, 9, 12, 11, 14, 13, 16, 15, 2, 1, 4, 3, 6, 5, 8, 7},
                {14, 13, 16, 15, 2, 1, 4, 3, 6, 5, 8, 7, 10, 9, 12, 11},

                {3, 4, 1, 2, 7, 8, 5, 6, 11, 12, 9, 10, 15, 16, 13, 14},
                {7, 8, 5, 6, 11, 12, 9, 10, 15, 16, 13, 14, 3, 4, 1, 2},
                {11, 12, 9, 10, 15, 16, 13, 14, 3, 4, 1, 2, 7, 8, 5, 6},
                {15, 16, 13, 14, 3, 4, 1, 2, 7, 8, 5, 6, 11, 12, 9, 10},

                {4, 3, 2, 1, 8, 7, 6, 5, 12, 11, 10, 9, 16, 15, 14, 13},
                {8, 7, 6, 5, 12, 11, 10, 9, 16, 15, 14, 13, 4, 3, 2, 1},
                {12, 11, 10, 9, 16, 15, 14, 13, 4, 3, 2, 1, 8, 7, 6, 5},
                {16, 15, 14, 13, 4, 3, 2, 1, 8, 7, 6, 5, 12, 11, 10, 9}
        };
    }
}

