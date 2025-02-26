package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.Localization;

import java.io.IOException;

/**
 * Controls the welcome screen where the user selects game settings before starting a new Sudoku game.
 * Manages UI text localization (via {@link Localization} and message bundles in the resource package),
 * dropdown selections, and transitions to the game board.
 */
public class WelcomeController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label boardSizeLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private ComboBox<String> sizeDropdown;
    @FXML
    private Button playButton;
    @FXML
    private Button rulesButton;

    /**
     * Initializes the welcome screen by setting localized text,
     * populating the board size selection dropdown, and configuring button actions.
     */
    @FXML
    public void initialize() {
        String welcomeText = Constants.UI.Texts.Labels.WELCOME;
        String boardSizeText = Constants.UI.Texts.Labels.BOARD_SIZE;
        String difficultyText = Constants.UI.Texts.Labels.DIFFICULTY;
        String playButtonText = Constants.UI.Texts.Buttons.PLAY;
        String rulesButtonText = Constants.UI.Texts.Buttons.RULES;

        welcomeLabel.setText(Localization.get(welcomeText));
        boardSizeLabel.setText(Localization.get(boardSizeText));
        difficultyLabel.setText(Localization.get(difficultyText));
        playButton.setText(Localization.get(playButtonText));
        rulesButton.setText(Localization.get(rulesButtonText));

        sizeDropdown.getItems().addAll(Constants.SudokuBoard.SIZE_OPTIONS);
        sizeDropdown.getSelectionModel().select(Constants.SudokuBoard.DEFAULT_SIZE);

        playButton.setOnAction(event -> loadGameBoard());
    }

    /**
     * Loads the Sudoku game board with the selected settings.
     * Retrieves the chosen board size from the dropdown, initializes the game controller,
     * and replaces the current window with the game board.
     */
    private void loadGameBoard() {
        String selectedSize = sizeDropdown.getValue();
        int size = Integer.parseInt(selectedSize.split("x")[0]);

        String viewFilePath = Constants.FilePaths.SUDOKU_VIEW;
        String title = Constants.UI.Texts.Titles.BOARD;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewFilePath));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            SudokuController controller = loader.getController();
            controller.initializeBoard(size);

            stage.setTitle(Localization.get(title, size));
            stage.setScene(scene);
            stage.show();

            ((Stage) playButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
