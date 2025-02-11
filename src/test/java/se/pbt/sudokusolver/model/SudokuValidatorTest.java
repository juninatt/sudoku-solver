package se.pbt.sudokusolver.model;

import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.models.SudokuValidator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SudokuValidatorTest {

    @Test
    void testValidBoard() {
        int[][] validBoard = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 4, 5, 2, 8, 6, 1, 7, 9}
        };

        SudokuBoard board = new SudokuBoard(9);
        fillBoard(board, validBoard);

        assertTrue(SudokuValidator.validateBoard(board), "Should be a correct board");
    }

    @Test
    void testInvalidBoard() {
        int[][] invalidBoard = {
                {5, 3, 4, 6, 7, 8, 9, 1, 2},
                {6, 7, 2, 1, 9, 5, 3, 4, 8},
                {1, 9, 8, 3, 4, 2, 5, 6, 7},
                {8, 5, 9, 7, 6, 1, 4, 2, 3},
                {4, 2, 6, 8, 5, 3, 7, 9, 1},
                {7, 1, 3, 9, 2, 4, 8, 5, 6},
                {9, 6, 1, 5, 3, 7, 2, 8, 4},
                {2, 8, 7, 4, 1, 9, 6, 3, 5},
                {3, 3, 5, 2, 8, 6, 1, 7, 9} // Error: Double "3" in last row
        };

        SudokuBoard board = new SudokuBoard(9);
        fillBoard(board, invalidBoard);

        assertFalse(SudokuValidator.validateBoard(board), "Should be a incorrect board");
    }

    private void fillBoard(SudokuBoard board, int[][] data) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                board.setValue(row, col, data[row][col]);
            }
        }
    }
}
