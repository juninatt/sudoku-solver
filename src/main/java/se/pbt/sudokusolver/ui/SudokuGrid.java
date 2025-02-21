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
    private final int[] subgridDimensions;
    private final SudokuViewModel viewModel;

    /**
     * Initializes the UI representation of the Sudoku board.
     * This includes setting up a grid structure with subgrids and binding
     * each cell to the ViewModel to ensure reactive updates.
     *
     * @param viewModel The {@link SudokuViewModel} managing game state and UI synchronization.
     */
    public SudokuGrid(SudokuViewModel viewModel) {
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
                subgridPane.getStyleClass().add(Constants.CSS.SUBGRID);

                addCells(subgridPane, subgridRow, subgridCol, subgridRows, subgridCols);

                gridPane.add(subgridPane, subgridCol, subgridRow);
            }
        }
    }

    /**
     * Populates the subgrid with individual cells, ensuring proper placement
     * within the larger Sudoku board. Each cell is linked to the ViewModel.
     */
    private void addCells(GridPane subgridPane, int subgridRow, int subgridCol, int subgridRows, int subgridCols) {
        int boardSize = viewModel.getBoardSize();

        for (int row = 0; row < subgridRows; row++) {
            for (int col = 0; col < subgridCols; col++) {
                int globalRow = subgridRow * subgridRows + row;
                int globalCol = subgridCol * subgridCols + col;

                if (globalRow < boardSize && globalCol < boardSize) {
                    TextField cell = createCell(globalRow, globalCol);
                    subgridPane.add(cell, col, row);
                }
            }
        }
    }

    /**
     * Creates a Sudoku cell and binds it to the ViewModel.
     * The cell listens for user input and updates the game state accordingly.
     * If the user presses ENTER, TAB, or leaves the cell, the value is updated.
     */
    private TextField createCell(int row, int col) {
        TextField cell = new TextField();
        cell.setPrefSize(40, 40);
        cell.setAlignment(javafx.geometry.Pos.CENTER);

        // Set the initial value from the ViewModel
        int value = viewModel.getCellValue(row, col);
        cell.setText(value == 0 ? "" : String.valueOf(value));

        // Common method to handle input validation
        Runnable updateCell = () -> {
            try {
                int newValue = Integer.parseInt(cell.getText().trim());
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
        };

        // Handles user input for the cell by validating and updating the game state.
        cell.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER, TAB -> updateCell.run();
                default -> {} // Ignore other key presses
            }
        });

        // Ensure the value is set when the user leaves the cell (clicks elsewhere)
        cell.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // If the focus is lost
                updateCell.run();
            }
        });

        return cell;
    }

    public GridPane getGridPane() {
        return gridPane;
    }
}
