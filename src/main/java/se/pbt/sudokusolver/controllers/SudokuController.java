package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import se.pbt.sudokusolver.services.SudokuService;

public class SudokuController {
    @FXML
    private GridPane gridPane;

    private final SudokuService sudokuService;

    public SudokuController() {
        sudokuService = new SudokuService();
    }


    public void initBoard(int size) {
        sudokuService.createBoard(size);
        gridPane.getChildren().clear();

        int subgridSize = (int) Math.sqrt(size);

        for (int subgridRow = 0; subgridRow < subgridSize; subgridRow++) {
            for (int subgridCol = 0; subgridCol < subgridSize; subgridCol++) {
                GridPane subgrid = new GridPane();
                subgrid.setGridLinesVisible(true);
                subgrid.setStyle("-fx-border-color: black; -fx-border-width: 2;");

                for (int row = 0; row < subgridSize; row++) {
                    for (int col = 0; col < subgridSize; col++) {
                        int globalRow = subgridRow * subgridSize + row;
                        int globalCol = subgridCol * subgridSize + col;

                        TextField cell = new TextField();
                        cell.setPrefSize(40, 40);
                        cell.setStyle("-fx-font-size: 16;");
                        cell.setAlignment(javafx.geometry.Pos.CENTER);

                        // Bind cell updates to SudokuService
                        cell.textProperty().addListener((observable, oldValue, newValue) -> {
                            if (!newValue.matches("[1-9]?")) {
                                cell.setText(oldValue);
                            } else if (!newValue.isEmpty()) {
                                sudokuService.setValue(globalRow, globalCol, Integer.parseInt(newValue));
                            } else {
                                sudokuService.setValue(globalRow, globalCol, 0);
                            }
                        });

                        TextField[][] cells = new TextField[size][size];
                        cells[globalRow][globalCol] = cell;
                        subgrid.add(cell, col, row);
                    }
                }

                gridPane.add(subgrid, subgridCol, subgridRow);
            }
        }
    }
}
