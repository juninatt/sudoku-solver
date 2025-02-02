package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import se.pbt.sudokusolver.models.SudokuBoard;

public class SudokuController {

    @FXML
    private GridPane gridPane;

    private SudokuBoard board;
    private TextField[][] cells;


    public void initBoard(int size) {
        board = new SudokuBoard(size);

        // Clear previous board if any
        gridPane.getChildren().clear();
        gridPane.setGridLinesVisible(false);

        int subgridSize = (int) Math.sqrt(size);
        cells = new TextField[size][size];

        // Create sub-grids
        for (int subgridRow = 0; subgridRow < subgridSize; subgridRow++) {
            for (int subgridCol = 0; subgridCol < subgridSize; subgridCol++) {
                GridPane subgrid = new GridPane();
                subgrid.setGridLinesVisible(true); // Show lines for sub-grids
                subgrid.setStyle("-fx-border-color: black; -fx-border-width: 2;");

                for (int row = 0; row < subgridSize; row++) {
                    for (int col = 0; col < subgridSize; col++) {
                        int globalRow = subgridRow * subgridSize + row;
                        int globalCol = subgridCol * subgridSize + col;

                        TextField cell = new TextField();
                        cell.setPrefSize(40, 40);
                        cell.setStyle("-fx-font-size: 16;");
                        cell.setAlignment(javafx.geometry.Pos.CENTER);

                        // Restrict input to valid Sudoku values
                        cell.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("[1-9]?")) {
                                cell.setText(oldValue);
                            } else if (!newValue.isEmpty()) {
                                int value = Integer.parseInt(newValue);
                                board.setValue(globalRow, globalCol, value);
                            } else {
                                board.setValue(globalRow, globalCol, 0);
                            }
                        });

                        cells[globalRow][globalCol] = cell;
                        subgrid.add(cell, col, row); // Add cell to subgrid
                    }
                }

                gridPane.add(subgrid, subgridCol, subgridRow); // Add subgrid to main grid
            }
        }
    }
}
