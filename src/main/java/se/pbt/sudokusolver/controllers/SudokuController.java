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

    SudokuViewModel viewModel;
    StringProperty[][] cells;
    int subgridSize;
    int subgridRow;
    int subgridCol;

    public void initBoard(int size) {
        viewModel = new SudokuViewModel(size);
        subgridSize = (int) Math.sqrt(size);
        cells = viewModel.getCells();

        for (subgridRow = 0; subgridRow < subgridSize; subgridRow++) {
            for (subgridCol = 0; subgridCol < subgridSize; subgridCol++) {
                GridPane subgrid = createSubgrid();
                addCellsToSubgrid(subgrid, size);
                gridPane.add(subgrid, subgridCol, subgridRow);
            }
        }
    }

    private GridPane createSubgrid() {
        GridPane subgrid = new GridPane();
        subgrid.setGridLinesVisible(true);
        subgrid.setStyle(Constants.CSS.STYLE_SUBGRID);
        return subgrid;
    }

    private void addCellsToSubgrid(GridPane subgrid, int size) {
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
        cell.setAlignment(javafx.geometry.Pos.CENTER);
        TextFormatter<Integer> formatter = new TextFormatter<>(new IntegerStringConverter(), 0, createFilter(size));
        cell.setTextFormatter(formatter);
        cell.textProperty().bindBidirectional(cellProperty);

        cellProperty.addListener((obs, oldVal, newVal) -> {
            if (!newVal.isEmpty() && !cell.getStyleClass().contains("filled-cell")) {
                cell.getStyleClass().add("filled-cell");
                cell.setEditable(false);
            } else if (newVal.isEmpty() && cell.getStyleClass().contains("filled-cell")) {
                cell.getStyleClass().remove("filled-cell");
                cell.setEditable(true);
            }
        });

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
