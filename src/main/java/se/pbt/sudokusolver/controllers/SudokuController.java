package se.pbt.sudokusolver.controllers;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

public class SudokuController {

    @FXML
    private GridPane gridPane;


    public void initBoard(int size) {
        SudokuViewModel viewModel = new SudokuViewModel(size);
        gridPane.getChildren().clear();

        StringProperty[][] cells = viewModel.getCells();

        int subgridSize = (int) Math.sqrt(size);

        for (int subgridRow = 0; subgridRow < subgridSize; subgridRow++) {
            for (int subgridCol = 0; subgridCol < subgridSize; subgridCol++) {
                GridPane subgrid = new GridPane();
                subgrid.setGridLinesVisible(true);
                subgrid.setStyle(Constants.CSS.STYLE_SUBGRID);

                for (int row = 0; row < subgridSize; row++) {
                    for (int col = 0; col < subgridSize; col++) {
                        int globalRow = subgridRow * subgridSize + row;
                        int globalCol = subgridCol * subgridSize + col;

                        TextField cell = new TextField();
                        cell.setPrefSize(40, 40);
                        cell.setStyle(Constants.CSS.STYLE_CELL);
                        cell.setAlignment(javafx.geometry.Pos.CENTER);

                        cell.textProperty().bindBidirectional(cells[globalRow][globalCol]);

                        subgrid.add(cell, col, row);
                    }
                }

                gridPane.add(subgrid, subgridCol, subgridRow);
            }
        }
    }
}
