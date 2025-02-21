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
 * Unit tests for {@link SudokuValidator} with a 12x12 Sudoku board.
 */
@DisplayName("12x12 Sudoku Board Tests")
class SudokuValidator12x12Test {

    private SudokuValidator validator;
    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard(12);
        validator = new SudokuValidator(board);
    }

    @Test
    @DisplayName("Should validate a correct 12x12 Sudoku board")
    void testValidSudokuBoard() {

        fillBoard(board, validBoard());
        assertTrue(validator.validateBoard(), "The 12x12 board should be valid.");
    }

    @Test
    @DisplayName("Should detect duplicate row in 12x12 Sudoku board")
    void testInvalidSudokuBoard_DuplicateRow() {
        int[][] invalidBoard = validBoard();
        invalidBoard[0] = invalidBoard[1]; // Duplicate first row

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 12x12 board should be invalid due to duplicate row.");
    }

    @Test
    @DisplayName("Should detect duplicate column in 12x12 Sudoku board")
    void testInvalidSudokuBoard_DuplicateColumn() {
        int[][] invalidBoard = validBoard();
        for (int i = 0; i < 12; i++) {
            invalidBoard[i][0] = invalidBoard[i][1]; // Duplicate first column
        }

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 12x12 board should be invalid due to duplicate column.");
    }

    @Test
    @DisplayName("Should detect duplicate subgrid in 12x12 Sudoku board")
    void testInvalidSudokuBoard_DuplicateSubgrid() {
        int[][] invalidBoard = validBoard();
        invalidBoard[0][0] = invalidBoard[1][1]; // Introduce duplicate in 3x4 subgrid

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 12x12 board should be invalid due to duplicate subgrid.");
    }

    private int[][] validBoard() {
        return new int[][] {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12},
                {5, 6, 7, 8, 9, 10, 11, 12, 1, 2, 3, 4},
                {9, 10, 11, 12, 1, 2, 3, 4, 5, 6, 7, 8},
                {2, 3, 4, 1, 6, 5, 8, 7, 10, 9, 12, 11},
                {6, 5, 8, 7, 10, 9, 12, 11, 2, 3, 4, 1},
                {10, 9, 12, 11, 2, 3, 4, 1, 6, 5, 8, 7},
                {3, 4, 1, 2, 7, 8, 5, 6, 11, 12, 9, 10},
                {7, 8, 5, 6, 11, 12, 9, 10, 3, 4, 1, 2},
                {11, 12, 9, 10, 3, 4, 1, 2, 7, 8, 5, 6},
                {4, 1, 2, 3, 8, 7, 6, 5, 12, 11, 10, 9},
                {8, 7, 6, 5, 12, 11, 10, 9, 4, 1, 2, 3},
                {12, 11, 10, 9, 4, 1, 2, 3, 8, 7, 6, 5}
        };
    }
}
