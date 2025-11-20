package se.pbt.sudokusolver.ui.view;

import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import se.pbt.sudokusolver.ui.factory.SudokuCellFactory;
import se.pbt.sudokusolver.ui.listener.CellUpdateListener;
import se.pbt.sudokusolver.ui.viewmodel.SudokuViewModel;

import static se.pbt.sudokusolver.ui.constants.UIConstants.*;

/**
 * Represents the UI component responsible for displaying the Sudoku board.
 * This class organizes subgrids and cells dynamically based on the board size.
 * Each cell is bound to the ViewModel, ensuring synchronization between UI and game logic.
 *
 * @see SudokuViewModel
 */
public class SudokuBoardView implements CellUpdateListener {

    private final GridPane gridPane;
    private final int[] subgridDimensions;
    private final SudokuViewModel viewModel;
    private final TextField[][] cellFields;


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

        viewModel.addCellUpdateListener(this);
        this.cellFields = new TextField[viewModel.getBoardSize()][viewModel.getBoardSize()];

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
                subgridPane.getStyleClass().add(CSS_CLASS_SUBGRID);

                populateSubgridWithCells(subgridPane, subgridRow, subgridCol, subgridRows, subgridCols);

                gridPane.add(subgridPane, subgridCol, subgridRow);
            }
        }
    }

    /**
     * Populates the subgrid with individual cells, ensuring proper placement
     * within the larger Sudoku board. Each cell is linked to the ViewModel.
     */
    private void populateSubgridWithCells(GridPane subgridPane, int subgridRow, int subgridCol, int subgridRows, int subgridCols) {
        int boardSize = viewModel.getBoardSize();

        for (int row = 0; row < subgridRows; row++) {
            for (int col = 0; col < subgridCols; col++) {
                int globalRow = subgridRow * subgridRows + row;
                int globalCol = subgridCol * subgridCols + col;

                if (globalRow < boardSize && globalCol < boardSize) {
                    TextField cell = SudokuCellFactory.create(globalRow, globalCol, viewModel);
                    cellFields[globalRow][globalCol] = cell;
                    subgridPane.add(cell, col, row);
                }
            }
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }


    @Override
    public void onCellUpdated(int row, int col, int newValue) {
        TextField cell = cellFields[row][col];
        cell.setText(newValue == EMPTY_CELL ? "" : String.valueOf(newValue));
        cell.setEditable(false);
        cell.getStyleClass().add(CSS_CLASS_FILLED_CELL);
    }
}
