package se.pbt.sudokusolver.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import se.pbt.sudokusolver.core.generation.SudokuBuilder;
import se.pbt.sudokusolver.core.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.core.generation.helpers.UniquenessChecker;
import se.pbt.sudokusolver.core.models.Difficulty;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.ui.view.SudokuBoardView;
import se.pbt.sudokusolver.core.validation.SudokuValidator;
import se.pbt.sudokusolver.ui.viewmodel.SudokuViewModel;

import java.io.IOException;

import static se.pbt.sudokusolver.shared.constants.Constants.PathConstants.MAIN_MENU_VIEW;
import static se.pbt.sudokusolver.shared.constants.Constants.UIConstants.I18N_TITLE_MAIN;

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
    private SudokuBoard solvedBoard;


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
     * Sets up the Sudoku board based on selected size and difficulty.
     */
    public void initializeBoard(int size, Difficulty difficulty) {
        SudokuBuilder sudokuBuilder = new SudokuBuilder(
                size,
                difficulty,
                new UniquenessChecker(),
                new SolutionGenerator()
        );

        SudokuBoard board = sudokuBuilder.buildSudokuPuzzle();
        this.solvedBoard = sudokuBuilder.getSolvedBoard();

        SudokuValidator validator = new SudokuValidator(board);

        this.viewModel = new SudokuViewModel(board, validator);
        SudokuBoardView sudokuBoardView = new SudokuBoardView(viewModel);

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
     * Reveals the correct value for the next empty cell in the board.
     * Updates the ViewModel and triggers validation if the board becomes full.
     */
    private void revealNextCorrectValue() {
        for (int row = 0; row < solvedBoard.getSize(); row++) {
            for (int col = 0; col < solvedBoard.getSize(); col++) {
                if (viewModel.isEmpty(row, col)) {
                    int correctValue = solvedBoard.getValueAt(row, col);
                    viewModel.forceSetValue(row, col, correctValue);

                    if (viewModel.isBoardFull()) {
                        viewModel.getValidator().validateBoard();
                    }

                    return;
                }
            }
        }
    }


    /**
     * Fills in the entire board with the correct values from the solved board.
     * Only fills cells that are currently empty, then triggers validation.
     */
    private void solveEntireBoard() {
        for (int row = 0; row < solvedBoard.getSize(); row++) {
            for (int col = 0; col < solvedBoard.getSize(); col++) {
                int value = solvedBoard.getValueAt(row, col);
                if (viewModel.isEmpty(row, col)) {
                    viewModel.forceSetValue(row, col, value);
                }
            }
        }
        viewModel.getValidator().validateBoard();
    }

    /**
     * Navigates back to the main menu.
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
            System.out.println(e.getMessage()); // TODO: Improve error handling
        }
    }

    /**
     * Sets whether cheat mode is enabled and activates related UI if true.
     * This must be called before board initialization to ensure correct setup.
     */
    public void setCheatMode(boolean cheatModeEnabled) {
        if (cheatModeEnabled) {
            enableCheatButtons();
        }
    }
}
