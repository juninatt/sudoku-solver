package se.pbt.sudokusolver.viewmodels;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import se.pbt.sudokusolver.services.SudokuService;

public class SudokuViewModel {
    private final SudokuService sudokuService;
    private final StringProperty[][] cells;

    public SudokuViewModel(int size) {
        this.sudokuService = new SudokuService();
        this.sudokuService.createBoard(size);

        this.cells = new StringProperty[size][size];
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells[row][col] = new SimpleStringProperty();
            }
        }
    }
    
    // Validates and commits a number to the board when the user confirms input with Enter or Tab.
    public void validateAndSetValue(TextField cell, int row, int col) {
        String input = cell.getText().trim();

        if (input.isEmpty()) {
            cells[row][col].set("");
            return;
        }

        try {
            int value = Integer.parseInt(input);

            if (isValidMove(value)) {
                cells[row][col].set(String.valueOf(value));
                sudokuService.setValue(row, col, value);
                cell.setEditable(false);
                cell.getStyleClass().add("filled-cell");
            } else {
                cell.clear();
            }
        } catch (NumberFormatException e) {
            cell.clear();
        }
    }

    public boolean isValidMove(int value) {
        return value >= 1 && value <= size;
    }

    public StringProperty[][] getCells() {
        return cells;
    }

}
