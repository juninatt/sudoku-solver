package se.pbt.sudokusolver.shared.listeners;

/**
 * UI listener for visual updates of individual Sudoku cells.
 * Used by the View layer to refresh a cell when its value changes.
 */
@FunctionalInterface
public interface CellViewListener {

    /**
     * Notifies that a cell's visual value should be updated.
     */
    void onCellUpdated(int row, int col, int newValue);
}


