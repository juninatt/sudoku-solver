package se.pbt.sudokusolver.ui.interaction;

import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.ui.viewmodel.SudokuViewModel;

import static se.pbt.sudokusolver.ui.constants.UIConstants.CSS_CLASS_FILLED_CELL;
import static se.pbt.sudokusolver.ui.constants.UIConstants.EMPTY_CELL;

/**
 * Handles user input events for editable Sudoku cells.
 * Applies changes through the ViewModel and restores UI state when inputs are invalid.
 */
public class CellEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(CellEventHandler.class);

    /**
     * Attaches listeners to a text field so that user input is validated and pushed to the ViewModel.
     */
    public void attachInputHandlers(TextField cell, int row, int col, SudokuViewModel viewModel) {
        Runnable updateCell = () -> handleInput(cell, row, col, viewModel);

        cell.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER, TAB -> updateCell.run();
                default -> {}
            }
        });

        cell.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) { // focus lost
                updateCell.run();
            }
        });
    }

    /**
     * Attempts to write user-provided input to the ViewModel.
     * Invalid input restores the cell's previous value.
     */
    private void handleInput(TextField cell, int row, int col, SudokuViewModel viewModel) {
        String trimmed = cell.getText().trim();

        try {
            int newValue = Integer.parseInt(trimmed);
            boolean success = viewModel.setValue(row, col, newValue);

            if (success) {
                logger.debug("Cell ({},{}) updated to {}", row, col, newValue);
                cell.setEditable(false);
                cell.getStyleClass().add(CSS_CLASS_FILLED_CELL);
            } else {
                logger.debug("Rejected cell update at ({},{}): invalid move", row, col);
                resetToModelValue(cell, row, col, viewModel);
            }

        } catch (NumberFormatException e) {
            logger.debug("Invalid input '{}' at ({},{}), reverting", trimmed, row, col);
            resetToModelValue(cell, row, col, viewModel);
        }
    }

    /**
     * Restores the UI value of the TextField to match the underlying model.
     * Clears the field if the value represents an empty cell.
     */
    private void resetToModelValue(TextField cell, int row, int col, SudokuViewModel viewModel) {
        int actualValue = viewModel.getCellValue(row, col);
        cell.setText(actualValue == EMPTY_CELL ? "" : String.valueOf(actualValue));
    }
}
