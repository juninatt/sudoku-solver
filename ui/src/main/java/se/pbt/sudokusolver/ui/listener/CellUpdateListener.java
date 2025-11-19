package se.pbt.sudokusolver.ui.listener;

/**
 * Listener interface for observing updates to individual cells in the Sudoku board.
 * Implementing classes will be notified when a cell's value is changed.
 */
public interface CellUpdateListener {

    /**
     * Called when a cell on the board is updated with a new value.
     */
    void onCellUpdated(int row, int col, int newValue);
}


