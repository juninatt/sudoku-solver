package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.models.SudokuValidator;
import se.pbt.sudokusolver.ui.SudokuGrid;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.Localization;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

import java.io.IOException;

/**
 * Manages the initialization of the Sudoku board and links the UI components
 * to the underlying game logic through the ViewModel.
 * Ensures that the game board is correctly instantiated and displayed.
 */
public class SudokuController {

    @FXML
    private GridPane gridPane;
    @FXML
    private Button homeButton;

    private SudokuViewModel viewModel;
    private SudokuGrid sudokuGrid;

    /**
     * Called automatically after FXML components are loaded.
     * Sets up event handlers for UI elements.
     */
    @FXML
    public void initialize() {
        homeButton.setGraphic(new FontIcon(FontAwesomeSolid.HOME));
        homeButton.setOnAction(event -> returnToMainMenu());
    }

    /**
     * Initializes the Sudoku board with the given size.
     * Creates the game model, ViewModel, and UI representation,
     * ensuring that all components are properly connected.
     *
     * @param size The dimension of the Sudoku board (e.g., 9 for a 9x9 grid).
     */
    public void initializeBoard(int size) {
        SudokuBoard sudokuBoard = new SudokuBoard(size, new SudokuValidator());
        viewModel = new SudokuViewModel(sudokuBoard);
        sudokuGrid = new SudokuGrid(viewModel);

        gridPane.getChildren().clear();
        gridPane.getChildren().add(sudokuGrid.getGridPane());
    }

    /**
     * Closes the current Sudoku window and returns to the main menu.
     */
    private void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FilePaths.WELCOME_VIEW));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(Localization.get("ui.welcome"));
            stage.show();

            // Close the current Sudoku window
            ((Stage) homeButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
