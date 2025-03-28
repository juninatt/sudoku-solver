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
 * Unit tests for {@link SudokuValidator} using a 25x25 board.
 * Ensures correct validation of rows, columns, and subgrids.
 */
@DisplayName("25x25 Sudoku Board Tests")
class SudokuValidator25x25Test {

    private SudokuValidator validator;
    private SudokuBoard board;

    @BeforeEach
    void setUp() {
        board = new SudokuBoard(25);
        validator = new SudokuValidator(board);
    }

    @Test
    @DisplayName("Should validate a correct 25x25 Sudoku board")
    void testValidSudokuBoard() {
        fillBoard(board, validBoard());
        assertTrue(validator.validateBoard(), "The 25x25 board should be valid.");
    }

    @Test
    @DisplayName("Should detect duplicate row in 25x25 Sudoku board")
    void testInvalidSudokuBoard_DuplicateRow() {
        int[][] invalidBoard = validBoard();
        invalidBoard[1] = invalidBoard[0]; // Introduces a duplicate row

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 25x25 board should be invalid due to duplicate rows.");
    }

    @Test
    @DisplayName("Should detect duplicate column in 25x25 Sudoku board")
    void testInvalidSudokuBoard_DuplicateColumn() {
        int[][] invalidBoard = validBoard();
        for (int i = 0; i < 25; i++) {
            invalidBoard[i][1] = invalidBoard[i][0]; // Introduces a duplicate column
        }

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 25x25 board should be invalid due to duplicate columns.");
    }

    @Test
    @DisplayName("Should detect duplicate subgrid in 25x25 Sudoku board")
    void testInvalidSudokuBoard_DuplicateSubgrid() {
        int[][] invalidBoard = validBoard();
        invalidBoard[4][4] = invalidBoard[0][0]; // Introduces a duplicate in the first 5x5 subgrid

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 25x25 board should be invalid due to duplicate subgrid.");
    }

    /**
     * Returns a valid 25x25 Sudoku board for testing.
     * Brädet följer standard 25x25 Sudoku-regler med 5x5 subgrids.
     */
    private int[][] validBoard() {
        return new int[][] {
                {  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25},
                {  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5},
                { 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10},
                { 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15},
                { 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20},

                {  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1},
                {  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6},
                { 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11},
                { 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16},
                { 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21},

                {  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2},
                {  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7},
                { 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12},
                { 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17},
                { 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22},

                {  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3},
                {  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8},
                { 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13},
                { 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18},
                { 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23},

                {  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4},
                { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9},
                { 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14},
                { 20, 21, 22, 23, 24, 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19},
                { 25,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24}
        };
    }
}
