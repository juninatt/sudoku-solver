package se.pbt.sudokusolver.viewmodels;

import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.utils.SudokuValidator;

/**
 * Acts as the intermediary between the UI and the underlying {@link SudokuBoard} model.
 * Provides safe access to board data and enables controlled modifications during gameplay.
 * This ViewModel allows the UI to query and update the Sudoku board while encapsulating validation logic.
 */
public class SudokuViewModel {
    private final SudokuBoard sudokuBoard;
    private final int boardSize;

    private final SudokuValidator validator;

    /**
     * Creates a ViewModel that serves as an intermediary between the UI and game logic.
     * It retrieves the board's initial values and calculates necessary grid properties,
     * ensuring that the UI can retrieve and modify data without directly affecting the {@link SudokuBoard}.
     */
    public SudokuViewModel(SudokuBoard sudokuBoard, SudokuValidator validator) {
        this.sudokuBoard = sudokuBoard;
        this.boardSize = sudokuBoard.getSize();
        this.validator = validator;
    }


    /**
     * Attempts to place a value in a specific cell on the board.
     * Ensures the move is within board limits before applying it.
     * Triggers validation once the board is fully filled.
     */
    public boolean setValue(int row, int col, int value) {
        if (isOutOfBounds(row, col, value) || sudokuBoard.hasValueAt(row, col)) {
            return false;
        }

        sudokuBoard.setValue(row, col, value);

        if (sudokuBoard.isBoardFull()) {
            validator.validateBoard();
        }

        return true;
    }

    /**
     * Validates that the specified row, column, and value are within the board's allowed range.
     */
    public boolean isOutOfBounds(int row, int col, int value) {
        return row < 0 || row >= boardSize || col < 0 || col >= boardSize || value < 1 || value > boardSize;
    }

    /**
     * Retrieves the current value of a specific cell.
     * Used by the UI to display numbers on the board.
     */
    public int getCellValue(int row, int col) {
        return sudokuBoard.getValueAt(row, col);
    }

    /**
     * Retrieves the board's size (e.g., 9 for a 9x9 Sudoku).
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Returns the width and height of a single subgrid.
     * Used to correctly format the UI representation of the board.
     */
    public int[] getSubgridDimensions() {
        return sudokuBoard.getSubgridDimensions();
    }
}
