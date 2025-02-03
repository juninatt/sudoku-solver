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
                int finalRow = row;
                int finalCol = col;

                cells[row][col].addListener((observable, oldValue, newValue) -> {
                    if (newValue.matches("[1-9]?")) {
                        int value = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                        sudokuService.setValue(finalRow, finalCol, value);
                    } else {
                        cells[finalRow][finalCol].set(oldValue);
                    }
                });
            }
        }
    }

    public StringProperty[][] getCells() {
        return cells;
    }

    public boolean isSolved() {
        return sudokuService.isSolved();
    }
}
