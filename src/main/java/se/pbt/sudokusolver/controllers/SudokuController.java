package se.pbt.sudokusolver.controllers;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.GridPane;
import javafx.util.converter.IntegerStringConverter;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

import java.util.function.UnaryOperator;

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
                GridPane subgrid = createSubgrid(subgridSize);
                addCellsToSubgrid(subgrid, cells, subgridRow, subgridCol, subgridSize, size);
                gridPane.add(subgrid, subgridCol, subgridRow);
            }
        }
    }

    private GridPane createSubgrid(int subgridSize) {
        GridPane subgrid = new GridPane();
        subgrid.setGridLinesVisible(true);
        subgrid.setStyle(Constants.CSS.STYLE_SUBGRID);
        return subgrid;
    }

    private void addCellsToSubgrid(GridPane subgrid, StringProperty[][] cells, int subgridRow, int subgridCol, int subgridSize, int size) {
        for (int row = 0; row < subgridSize; row++) {
            for (int col = 0; col < subgridSize; col++) {
                int globalRow = subgridRow * subgridSize + row;
                int globalCol = subgridCol * subgridSize + col;
                TextField cell = createCell(cells[globalRow][globalCol], size);
                subgrid.add(cell, col, row);
            }
        }
    }

    private TextField createCell(StringProperty cellProperty, int size) {
        TextField cell = new TextField();
        cell.setPrefSize(40, 40);
        cell.setStyle(Constants.CSS.STYLE_CELL);
        cell.setAlignment(javafx.geometry.Pos.CENTER);
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 0, createFilter(size));
        cell.setTextFormatter(formatter);
        cell.textProperty().bindBidirectional(cellProperty);
        return cell;
    }

    private UnaryOperator<Change> createFilter(int size) {
        return change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[1-" + size + "]?")) {
                return change;
            }
            return null;
        };
    }
}
