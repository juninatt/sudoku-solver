package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import se.pbt.sudokusolver.builders.SudokuBuilder;
import se.pbt.sudokusolver.builders.helpers.SolutionGenerator;
import se.pbt.sudokusolver.builders.helpers.UniquenessChecker;
import se.pbt.sudokusolver.models.Difficulty;
import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.ui.SudokuBoardView;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.SudokuValidator;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

import java.io.IOException;

/**
 * Manages the initialization of the Sudoku board and links the UI components
 * to the underlying game logic through the ViewModel.
 * Ensures that the game board is correctly instantiated and displayed.
 */
public class SudokuGameController {

    @FXML
    private GridPane gridPane;
    @FXML
    private Button homeButton;

    private SudokuViewModel viewModel;
    private SudokuBoardView sudokuBoardView;

    /**
     * Initializes UI components after FXML is loaded.
     * Adds an icon to the home button and sets up its event handler
     * to allow users to navigate back to the main menu.
     */
    @FXML
    public void initialize() {
        FontIcon homeIcon = new FontIcon(FontAwesomeSolid.HOME);
        homeIcon.setIconSize(20);
        homeButton.setGraphic(homeIcon);

        homeButton.setOnAction(event -> returnToMainMenu());
    }

    /**
     * Initializes the Sudoku board with the given size.
     * Creates the game model, ViewModel, and UI representation,
     * ensuring that all components are properly connected.
     *
     * @param size The dimension of the Sudoku board (e.g., 9 for a 9x9 grid).
     */
    public void initializeBoard(int size, Difficulty difficulty) {
        SudokuBuilder sudokuBuilder = new SudokuBuilder(
                size,
                difficulty,
                new UniquenessChecker(),
                new SolutionGenerator());

        SudokuBoard board = sudokuBuilder.buildSudokuPuzzle();
        SudokuValidator validator = new SudokuValidator(board);

        viewModel = new SudokuViewModel(board, validator);
        sudokuBoardView = new SudokuBoardView(viewModel);

        gridPane.getChildren().clear();
        gridPane.getChildren().add(sudokuBoardView.getGridPane());
    }

    /**
     * Closes the current Sudoku window and returns to the main menu.
     */
    private void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FilePaths.WELCOME_VIEW));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(Constants.UI.Texts.Titles.MAIN);
            stage.show();

            ((Stage) homeButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
