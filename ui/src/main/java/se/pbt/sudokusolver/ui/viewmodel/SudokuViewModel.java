package se.pbt.sudokusolver.ui.viewmodel;

import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.ui.listener.CellUpdateListener;
import se.pbt.sudokusolver.validation.SudokuValidator;

import java.util.ArrayList;
import java.util.List;

import static se.pbt.sudokusolver.shared.constants.Constants.GameConstants.EMPTY_CELL;
import static se.pbt.sudokusolver.shared.constants.Constants.UIConstants.MIN_CELL_VALUE;

/**
 * Serves as the connection layer between the UI and the underlying {@link SudokuBoard}.
 * Centralizes read and write operations so the UI can interact with the board safely,
 * while keeping validation and game-state updates contained within one place.
 */
public class SudokuViewModel {
    private final SudokuBoard sudokuBoard;
    private final int boardSize;

    private final SudokuValidator validator;

    private final List<CellUpdateListener> listeners = new ArrayList<>();

    public void addCellUpdateListener(CellUpdateListener listener) {
        listeners.add(listener);
    }

    private void notifyCellUpdated(int row, int col, int newValue) {
        for (CellUpdateListener listener : listeners) {
            listener.onCellUpdated(row, col, newValue);
        }
    }

    /**
     * Sets up the ViewModel as the link between UI and game logic.
     * Stores the board and validator so that gameplay updates can be applied and checked in a controlled way.
     */
    public SudokuViewModel(SudokuBoard sudokuBoard, SudokuValidator validator) {
        this.sudokuBoard = sudokuBoard;
        this.boardSize = sudokuBoard.getSize();
        this.validator = validator;
    }


    /**
     * Attempts to write a value to a cell if it is within bounds and currently empty.
     * Performs a full-board validation once the last cell is filled to check for a completed solution.
     */
    public boolean setValue(int row, int col, int value) {
        if (isOutOfBounds(row, col, value) || sudokuBoard.getValueAt(row, col) == EMPTY_CELL) {
            return false;
        }

        sudokuBoard.setValue(row, col, value);

        if (sudokuBoard.isBoardFull()) {
            validator.validateBoard();
        }

        return true;
    }

    /**
     * Verifies that the target row, column, and value are within allowed limits.
     * Prevents illegal board access before any move is applied.
     */
    public boolean isOutOfBounds(int row, int col, int value) {
        return row < 0
                || row >= boardSize
                || col < 0
                || col >= boardSize
                || value < MIN_CELL_VALUE
                || value > boardSize;
    }


    /**
     * Writes a value directly to a cell without any rule checks.
     * Intended for features like hints or cheat mode and immediately notifies UI listeners.
     */
    public void forceSetValue(int row, int col, int value) {

        sudokuBoard.setValue(row, col, value);
        notifyCellUpdated(row, col, value);
    }

    public SudokuBoard getBoard() {
        return sudokuBoard;
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

    /**
     * Retrieves the current value of a specific cell.
     * Used by the UI to display numbers on the board.
     */
    public int getCellValue(int row, int col) {
        return sudokuBoard.getValueAt(row, col);
    }


    public SudokuValidator getValidator() {
        return validator;
    }

    public boolean isBoardFull() {
        return sudokuBoard.isBoardFull();
    }

}
