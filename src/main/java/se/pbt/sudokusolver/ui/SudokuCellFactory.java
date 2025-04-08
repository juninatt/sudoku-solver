package se.pbt.sudokusolver.ui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

/**
 * Factory class responsible for creating Sudoku cell components (TextFields)
 * based on their initial values and purpose within the game.
 * Cells may represent immutable clues (pre-filled numbers) or editable fields for user input.
 */
public class SudokuCellFactory {

    /**
     * Creates a Sudoku cell (TextField) at the specified board position,
     * using the current value in the ViewModel to determine its type.
     */
    public static TextField create(int row, int col, SudokuViewModel viewModel) {
        int value = viewModel.getCellValue(row, col);
        return (value != 0)
                ? createClueCell(value)
                : createEmptyCell(row, col, viewModel);
    }

    /**
     * Creates a non-editable cell representing a pre-filled clue.
     */
    private static TextField createClueCell(int value) {
        TextField cell = new TextField(String.valueOf(value));
        cell.setPrefSize(40, 40);
        cell.setAlignment(Pos.CENTER);
        cell.setEditable(false);
        cell.getStyleClass().add(Constants.UI.CSS.FILLED_CELL);
        return cell;
    }

    /**
     * Creates an editable cell for user input and attaches validation logic.
     */
    private static TextField createEmptyCell(int row, int col, SudokuViewModel viewModel) {
        TextField cell = new TextField();
        cell.setPrefSize(40, 40);
        cell.setAlignment(Pos.CENTER);

        attachInputHandlers(cell, row, col, viewModel);
        return cell;
    }

    /**
     * Attaches input listeners to the cell for handling updates on user input.
     */
    private static void attachInputHandlers(TextField cell, int row, int col, SudokuViewModel viewModel) {
        Runnable updateCell = () -> handleInput(cell, row, col, viewModel);

        cell.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER, TAB -> updateCell.run();
                default -> {}
            }
        });

        cell.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                updateCell.run();
            }
        });
    }

    /**
     * Handles user input by attempting to update the ViewModel.
     * Restores the previous value if the update is invalid.
     */
    private static void handleInput(TextField cell, int row, int col, SudokuViewModel viewModel) {
        try {
            int newValue = Integer.parseInt(cell.getText().trim());
            boolean success = viewModel.setValue(row, col, newValue);

            if (success) {
                cell.setEditable(false);
                cell.getStyleClass().add(Constants.UI.CSS.FILLED_CELL);
            } else {
                resetToModelValue(cell, row, col, viewModel);
            }
        } catch (NumberFormatException e) {
            resetToModelValue(cell, row, col, viewModel);
        }
    }

    /**
     * Restores the cell's content to reflect the current value from the ViewModel.
     */
    private static void resetToModelValue(TextField cell, int row, int col, SudokuViewModel viewModel) {
        int actualValue = viewModel.getCellValue(row, col);
        cell.setText(actualValue == 0 ? "" : String.valueOf(actualValue));
    }
}
