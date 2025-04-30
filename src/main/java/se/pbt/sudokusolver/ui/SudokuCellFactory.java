package se.pbt.sudokusolver.ui;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

import static se.pbt.sudokusolver.utils.Constants.GameConstants.EMPTY_CELL;
import static se.pbt.sudokusolver.utils.Constants.UIConstants.CELL_SIZE;
import static se.pbt.sudokusolver.utils.Constants.UIConstants.CSS_CLASS_FILLED_CELL;

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
        return (value != EMPTY_CELL)
                ? createClueCell(value)
                : createEmptyCell(row, col, viewModel);
    }

    /**
     * Creates a non-editable cell pre-filled with a number (a clue).
     * These cells are styled and locked to prevent editing.
     */
    private static TextField createClueCell(int value) {
        TextField cell = new TextField(String.valueOf(value));
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setAlignment(Pos.CENTER);
        cell.setEditable(false);
        cell.getStyleClass().add(CSS_CLASS_FILLED_CELL);
        return cell;
    }

    /**
     * Creates an editable cell for user input and attaches validation logic.
     */
    private static TextField createEmptyCell(int row, int col, SudokuViewModel viewModel) {
        TextField cell = new TextField();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
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
                cell.getStyleClass().add(CSS_CLASS_FILLED_CELL);
            } else {
                resetToModelValue(cell, row, col, viewModel);
            }
        } catch (NumberFormatException e) {
            resetToModelValue(cell, row, col, viewModel);
        }
    }

    /**
     * Resets the cell's text content to reflect the latest value in the ViewModel.
     * If the cell is empty (0), the field is cleared.
     */
    private static void resetToModelValue(TextField cell, int row, int col, SudokuViewModel viewModel) {
        int actualValue = viewModel.getCellValue(row, col);
        cell.setText(actualValue == EMPTY_CELL ? "" : String.valueOf(actualValue));
    }
}
