package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.models.SudokuValidator;
import se.pbt.sudokusolver.ui.SudokuGrid;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

/**
 * Manages the initialization of the Sudoku board and links the UI components
 * to the underlying game logic through the ViewModel.
 * Ensures that the game board is correctly instantiated and displayed.
 */
public class SudokuController {

    @FXML
    private GridPane gridPane;

    private SudokuViewModel viewModel;
    private SudokuGrid sudokuGrid;

    /**
     * Initializes the Sudoku board with the given size.
     * Creates the game model, ViewModel, and UI representation,
     * ensuring that all components are properly connected.
     *
     * @param size The dimension of the Sudoku board (e.g., 9 for a 9x9 grid).
     */
    public void initialize(int size) {
        SudokuBoard sudokuBoard = new SudokuBoard(size, new SudokuValidator());
        viewModel = new SudokuViewModel(sudokuBoard);
        sudokuGrid = new SudokuGrid(viewModel);

        gridPane.getChildren().clear();
        gridPane.getChildren().add(sudokuGrid.getGridPane());
    }
}
