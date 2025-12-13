package se.pbt.sudokusolver.ui.view;

import javafx.geometry.Pos;
import javafx.scene.control.TextField;

import static se.pbt.sudokusolver.ui.constants.UIConstants.CELL_SIZE;
import static se.pbt.sudokusolver.ui.constants.UIConstants.CSS_CLASS_FILLED_CELL;

/**
 * Factory class responsible for creating Sudoku cell components (TextFields)
 * based on their initial values and purpose within the game.
 * Cells may represent immutable clues (pre-filled numbers) or editable fields for user input.
 */
public class CellFactory {

    /**
     * Creates a non-editable cell pre-filled with a number (a clue).
     * These cells are styled and locked to prevent editing.
     */
    public TextField createClueCell(int number) {
        TextField cell = createBaseCell();
        setNumberAndLockCell(cell, number);
        return cell;
    }

    /**
     * Creates a basic empty cell configured with default size and alignment.
     * Callers are responsible for setting content and editability.
     */
    public TextField createBaseCell() {
        TextField cell = new TextField();
        cell.setPrefSize(CELL_SIZE, CELL_SIZE);
        cell.setAlignment(Pos.CENTER);

        return cell;
    }

    /**
     * Sets the given number in the cell and marks it as a non-editable filled cell.
     * Applies the appropriate style to visually distinguish it from user-editable cells.
     */
    private void setNumberAndLockCell(TextField cell, int number) {
        cell.setText(String.valueOf(number));
        cell.setEditable(false);
        cell.getStyleClass().add(CSS_CLASS_FILLED_CELL);
    }
}
