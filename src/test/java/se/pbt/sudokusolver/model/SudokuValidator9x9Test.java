package se.pbt.sudokusolver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.models.SudokuValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static se.pbt.sudokusolver.utils.SudokuTestBoardFactory.fillBoard;

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

        fillBoard(board, validBoard);
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

        fillBoard(board, invalidBoard);
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

        fillBoard(board, invalidBoard);
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

        fillBoard(board, invalidBoard);
        assertFalse(validator.validateBoard(), "The board should be invalid due to duplicate subgrid.");
    }
}
