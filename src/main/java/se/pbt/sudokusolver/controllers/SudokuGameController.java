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
import se.pbt.sudokusolver.utils.SudokuValidator;
import se.pbt.sudokusolver.viewmodels.SudokuViewModel;

import java.io.IOException;

import static se.pbt.sudokusolver.utils.Constants.PathConstants.MAIN_MENU_VIEW;
import static se.pbt.sudokusolver.utils.Constants.UIConstants.I18N_TITLE_MAIN;

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
    @FXML
    private HBox cheatButtonsBox;
    @FXML
    private Button nextHintButton; // TODO: Implement message bundle
    @FXML
    private Button solveBoardButton; // TODO: Implement message bundle

    private SudokuViewModel viewModel;
    private SudokuBoardView sudokuBoardView;

    /**
     * Initializes basic UI logic after FXML is loaded.
     */
    @FXML
    public void initialize() {
        FontIcon homeIcon = new FontIcon(FontAwesomeSolid.HOME);
        homeIcon.setIconSize(20);
        homeButton.setGraphic(homeIcon);

        homeButton.setOnAction(event -> returnToMainMenu());

        cheatButtonsBox.setVisible(false);
        cheatButtonsBox.setManaged(false);
    }

    /**
     * Sets whether cheat mode is enabled and activates related UI.
     */
    public void setCheatMode(boolean cheatModeEnabled) {
        if (cheatModeEnabled) {
            enableCheatButtons();
        }
    }

    /**
     * Sets up the Sudoku board based on selected size and difficulty.
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
     * Displays the cheat buttons and connects their event handlers.
     */
    private void enableCheatButtons() {
        cheatButtonsBox.setVisible(true);
        cheatButtonsBox.setManaged(true);

        nextHintButton.setOnAction(event -> revealNextCorrectValue());
        solveBoardButton.setOnAction(event -> solveEntireBoard());
    }

    /**
     * Closes the current Sudoku window and returns to the main menu.
     */
    private void returnToMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_VIEW));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(I18N_TITLE_MAIN);
            stage.show();

            ((Stage) homeButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
