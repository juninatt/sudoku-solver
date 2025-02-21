package se.pbt.sudokusolver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.models.SudokuValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.pbt.sudokusolver.utils.SudokuTestBoardFactory.fillBoard;

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

        fillBoard(board, validBoard);
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

        fillBoard(board, invalidBoard);
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

        fillBoard(board, invalidBoard);
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

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The 6x6 board should be invalid due to duplicate subgrid.");
    }
}
