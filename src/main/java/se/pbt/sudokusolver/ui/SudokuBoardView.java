package se.pbt.sudokusolver.ui;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

/**
 * Represents the UI component responsible for displaying the Sudoku board.
 * This class organizes subgrids and cells dynamically based on the board size.
 * Each cell is bound to the ViewModel, ensuring synchronization between UI and game logic.
 *
 * @see SudokuViewModel
 */
public class SudokuBoardView {
    private final GridPane gridPane;
    private final int[] subgridDimensions;
    private final SudokuViewModel viewModel;

    /**
     * Initializes the UI representation of the Sudoku board.
     * This includes setting up a grid structure with subgrids and binding
     * each cell to the ViewModel to ensure reactive updates.
     *
     * @param viewModel The {@link SudokuViewModel} managing game state and UI synchronization.
     */
    public SudokuBoardView(SudokuViewModel viewModel) {
        this.viewModel = viewModel;
        this.subgridDimensions = viewModel.getSubgridDimensions();
        this.gridPane = new GridPane();

        setupGrid();
    }

    /**
     * Constructs the Sudoku board by arranging subgrids according to Sudoku rules.
     * The grid is dynamically adjusted based on the selected board size.
     */
    private void setupGrid() {
        int subgridRows = subgridDimensions[0];
        int subgridCols = subgridDimensions[1];
        int boardSize = viewModel.getBoardSize();

        for (int subgridRow = 0; subgridRow < boardSize / subgridRows; subgridRow++) {
            for (int subgridCol = 0; subgridCol < boardSize / subgridCols; subgridCol++) {
                GridPane subgridPane = new GridPane();
                subgridPane.setGridLinesVisible(true);
                subgridPane.getStyleClass().add(Constants.UI.CSS.SUBGRID);

                addCellsToSubgrid(subgridPane, subgridRow, subgridCol, subgridRows, subgridCols);

                gridPane.add(subgridPane, subgridCol, subgridRow);
            }
        }
    }

    /**
     * Populates the subgrid with individual cells, ensuring proper placement
     * within the larger Sudoku board. Each cell is linked to the ViewModel.
     */
    private void addCellsToSubgrid(GridPane subgridPane, int subgridRow, int subgridCol, int subgridRows, int subgridCols) {
        int boardSize = viewModel.getBoardSize();

        for (int row = 0; row < subgridRows; row++) {
            for (int col = 0; col < subgridCols; col++) {
                int globalRow = subgridRow * subgridRows + row;
                int globalCol = subgridCol * subgridCols + col;

                if (globalRow < boardSize && globalCol < boardSize) {
                    TextField cell = SudokuCellFactory.create(globalRow, globalCol, viewModel);
                    subgridPane.add(cell, col, row);
                }
            }
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
