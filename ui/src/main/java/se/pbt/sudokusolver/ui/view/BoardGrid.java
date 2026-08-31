package se.pbt.sudokusolver.ui.view;

import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import se.pbt.sudokusolver.shared.listeners.CellViewListener;
import se.pbt.sudokusolver.shared.listeners.RuleViolationScope;
import se.pbt.sudokusolver.ui.viewmodel.SudokuViewModel;

import static se.pbt.sudokusolver.shared.constants.SharedConstants.EMPTY_CELL;
import static se.pbt.sudokusolver.ui.constants.UIConstants.*;

/**
 * Represents the UI component responsible for displaying the Sudoku board.
 * This class organizes subgrids and cells dynamically based on the board size.
 * Each cell is bound to the ViewModel, ensuring synchronization between UI and game logic.
 *
 * @see SudokuViewModel
 */
public class BoardGrid implements CellViewListener {

    private final GridPane gridPane;
    private final int[] subGrid;
    private final TextField[][] cellFields;
    private final GridPane[][] subgridPanes;
    private final int size;

    private final SudokuViewModel viewModel;


    /**
     * Initializes the UI representation of the Sudoku board.
     * This includes setting up a grid structure with subgrids and binding
     * each cell to the ViewModel to ensure reactive updates.
     */
    public BoardGrid(int size, SudokuViewModel viewModel) {
        this.viewModel = viewModel;
        this.size = size;
        this.subGrid = viewModel.getSubGridDimensions();
        this.gridPane = new GridPane();
        this.cellFields = new TextField[size][size];
        this.subgridPanes = new GridPane[size / subGrid[0]][size / subGrid[1]];
    }

    /**
     * Constructs the Sudoku board by arranging subgrids according to Sudoku rules.
     * The grid is dynamically adjusted based on the selected board size.
     */
    public void setupGrid() {
        int subgridRows = subGrid[0];
        int subgridCols = subGrid[1];

        for (int subgridRow = 0; subgridRow < size / subgridRows; subgridRow++) {
            for (int subgridCol = 0; subgridCol < size / subgridCols; subgridCol++) {
                GridPane subgridPane = new GridPane();
                subgridPane.setGridLinesVisible(true);
                subgridPane.getStyleClass().add(CSS_CLASS_SUBGRID);

                populateSubgridWithCells(subgridPane, subgridRow, subgridCol, subgridRows, subgridCols);

                subgridPanes[subgridRow][subgridCol] = subgridPane;
                gridPane.add(subgridPane, subgridCol, subgridRow);
            }
        }
    }

    /**
     * Populates the subgrid with individual cells, ensuring proper placement
     * within the larger Sudoku board. Each cell is linked to the ViewModel.
     */
    private void populateSubgridWithCells(GridPane subgridPane, int subgridRow, int subgridCol, int subgridRows, int subgridCols) {
        for (int row = 0; row < subgridRows; row++) {
            for (int col = 0; col < subgridCols; col++) {
                int globalRow = subgridRow * subgridRows + row;
                int globalCol = subgridCol * subgridCols + col;

                if (globalRow < size && globalCol < size) {
                    TextField cell = viewModel.createCell(globalRow, globalCol);
                    cellFields[globalRow][globalCol] = cell;
                    subgridPane.add(cell, col, row);
                }
            }
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }


    /**
     * Reflects a value change from {@link se.pbt.sudokusolver.game.service.GameService} in the UI.
     * A non-empty value locks and styles the cell as filled. An empty value (a cheat-mode revert
     * of a rule-breaking move) instead makes the cell editable again so the player can retry,
     * clearing any leftover "filled" styling.
     */
    @Override
    public void onCellUpdated(int row, int col, int newValue) {
        TextField cell = cellFields[row][col];
        boolean isEmpty = newValue == EMPTY_CELL;

        cell.setText(isEmpty ? "" : String.valueOf(newValue));
        cell.setEditable(isEmpty);
        cell.getStyleClass().remove(CSS_CLASS_FILLED_CELL);
        if (!isEmpty) {
            cell.getStyleClass().add(CSS_CLASS_FILLED_CELL);
        }
    }

    /**
     * Briefly highlights a cell that just broke Sudoku rules (cheat mode), then removes the
     * highlight so the player can see and retry the mistake without a permanent visual mark.
     */
    public void flagInvalidMove(int row, int col) {
        TextField cell = cellFields[row][col];
        cell.getStyleClass().add(CSS_CLASS_INVALID_CELL);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(event -> cell.getStyleClass().remove(CSS_CLASS_INVALID_CELL));
        pause.play();
    }

    /**
     * Permanently marks the cell that ended the game (end-on-mistake mode) so the player can
     * see exactly which move was the mistake. Unlike {@link #flagInvalidMove}, this highlight
     * does not fade, since the board is locked afterward and there is no "retry" to clear it for.
     */
    public void markMistake(int row, int col) {
        TextField cell = cellFields[row][col];
        cell.getStyleClass().remove(CSS_CLASS_FILLED_CELL);
        cell.getStyleClass().add(CSS_CLASS_INVALID_CELL);
    }

    /**
     * Frames the row and/or subgrid that caused an end-on-mistake violation with a red border
     * around their cells, alongside the individually marked offending cell from {@link #markMistake}.
     * Unlike that cell's solid highlight, this only outlines the group so the rest of its values
     * stay legible.
     */
    public void frameViolatedUnits(int row, int col, RuleViolationScope scope) {
        if (scope.rowViolated()) {
            frameRow(row);
        }
        if (scope.subgridViolated()) {
            frameSubgrid(row, col);
        }
    }

    /**
     * Outlines an entire row with a red border by giving each of its cells the matching top/bottom
     * (and, for the first/last cell, left/right) edge so the row reads as one framed rectangle even
     * though its cells belong to different subgrid panes.
     */
    private void frameRow(int row) {
        for (int col = 0; col < size; col++) {
            TextField cell = cellFields[row][col];
            String edgeStyleClass = (col == 0) ? CSS_CLASS_ROW_VIOLATION_START
                    : (col == size - 1) ? CSS_CLASS_ROW_VIOLATION_END
                    : CSS_CLASS_ROW_VIOLATION_MIDDLE;
            cell.getStyleClass().add(edgeStyleClass);
        }
    }

    /**
     * Outlines the subgrid containing {@code (row, col)}. Unlike a row, a subgrid is already a
     * single container node, so it can be framed directly instead of styling individual cells.
     */
    private void frameSubgrid(int row, int col) {
        int subgridRows = subGrid[0];
        int subgridCols = subGrid[1];
        subgridPanes[row / subgridRows][col / subgridCols].getStyleClass().add(CSS_CLASS_SUBGRID_VIOLATION);
    }

    /**
     * Locks every editable cell on the board. Used when the game ends (end-on-mistake mode)
     * to prevent further input even though {@link se.pbt.sudokusolver.game.service.GameService}
     * already rejects moves once the game is over.
     */
    public void disableAllCells() {
        for (TextField[] row : cellFields) {
            for (TextField cell : row) {
                if (cell != null) {
                    cell.setEditable(false);
                }
            }
        }
    }
}