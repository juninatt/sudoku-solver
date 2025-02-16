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
public class SudokuGrid {
    private final GridPane gridPane;
    private final int subgridSize;
    private final SudokuViewModel viewModel;

    /**
     * Initializes the UI representation of the Sudoku board.
     * This includes setting up a grid structure with subgrids and binding
     * each cell to the ViewModel to ensure reactive updates.
     *
     * @param viewModel  The {@link SudokuViewModel} managing game state and UI synchronization.
     */
    public SudokuGrid(SudokuViewModel viewModel) {
        this.viewModel = viewModel;
        this.subgridSize = viewModel.getSubgridSize();
        this.gridPane = new GridPane();

        setupGrid();
    }

    /**
     * Constructs the Sudoku board by arranging subgrids according to Sudoku rules.
     * The grid is dynamically adjusted based on the selected board size.
     */
    private void setupGrid() {
        for (int subgridRow = 0; subgridRow < subgridSize; subgridRow++) {
            for (int subgridCol = 0; subgridCol < subgridSize; subgridCol++) {
                SubGrid subgrid = new SubGrid(subgridRow, subgridCol);
                gridPane.add(subgrid.getGridPane(), subgridCol, subgridRow);
            }
        }
    }


    public GridPane getGridPane() {
        return gridPane;
    }

    /**
     * Represents the UI of an individual subgrid within the Sudoku board.
     * Handles its own cells and data binding with the ViewModel.
     */
    private class SubGrid {
        private final GridPane subgridPane;

        /**
         * Constructs a subgrid and initializes its cells.
         * Each subgrid visually represents a section of the full Sudoku board.
         */
        public SubGrid(int subgridRow, int subgridCol) {
            this.subgridPane = new GridPane();
            this.subgridPane.setGridLinesVisible(true);
            this.subgridPane.getStyleClass().add(Constants.CSS.SUBGRID);

            addCells(subgridRow, subgridCol);
        }

        /**
         * Populates the subgrid with individual cells, ensuring proper placement
         * within the larger Sudoku board. Each cell is linked to the ViewModel.
         */
        private void addCells(int subgridRow, int subgridCol) {
            int size = viewModel.getBoardSize();
            for (int row = 0; row < subgridSize; row++) {
                for (int col = 0; col < subgridSize; col++) {
                    int globalRow = subgridRow * subgridSize + row;
                    int globalCol = subgridCol * subgridSize + col;

                    if (globalRow < size && globalCol < size) {
                        TextField cell = createCell(globalRow, globalCol);
                        subgridPane.add(cell, col, row);
                    }
                }
            }
        }

        /**
         * Creates a Sudoku cell and binds it to the ViewModel.
         * The cell listens for user input and updates the game state accordingly.
         */
        private TextField createCell(int row, int col) {
            TextField cell = new TextField();
            cell.setPrefSize(40, 40);
            cell.setAlignment(javafx.geometry.Pos.CENTER);

            // Set the initial value from the ViewModel
            int value = viewModel.getCellValue(row, col);
            cell.setText(value == 0 ? "" : String.valueOf(value));


             // Handles user input for the cell by validating and updating the game state.
             // If the input is valid, the cell becomes non-editable and is styled; otherwise, it is cleared.
            cell.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case ENTER, TAB -> {
                        try {
                            int newValue = Integer.parseInt(cell.getText());
                            boolean success = viewModel.setValue(row, col, newValue);

                            if (success) {
                                cell.setEditable(false);
                                cell.getStyleClass().add(Constants.CSS.FILLED_CELL);
                            } else {
                                cell.clear();
                            }
                        } catch (NumberFormatException e) {
                            cell.clear();
                        }
                    }
                    default -> {} // Ignore other key presses
                }
            });

            return cell;
        }

        public GridPane getGridPane() {
            return subgridPane;
        }
    }
}
