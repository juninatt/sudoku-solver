package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import se.pbt.sudokusolver.models.Difficulty;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.Localization;

import java.io.IOException;

import static se.pbt.sudokusolver.utils.Constants.GameConstants.*;
import static se.pbt.sudokusolver.utils.Constants.UIConstants.*;

/**
 * Controls the welcome screen where the user selects game settings before starting a new Sudoku game.
 * Manages UI text localization (via {@link Localization} and message bundles in the resource package),
 * dropdown selections, and transitions to the game board.
 */
public class MainMenuController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label boardSizeLabel;
    @FXML
    private Label difficultyLabel;
    @FXML
    private ComboBox<Difficulty> difficultyDropdown;
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

        welcomeLabel.setText(Localization.get(I18N_LABEL_WELCOME));
        boardSizeLabel.setText(Localization.get(I18N_LABEL_BOARD_SIZE));
        difficultyLabel.setText(Localization.get(I18N_LABEL_DIFFICULTY));
        playButton.setText(Localization.get(I18N_BUTTON_PLAY));
        rulesButton.setText(Localization.get(I18N_BUTTON_RULES));

        sizeDropdown.getItems().addAll(BOARD_SIZE_OPTIONS);
        sizeDropdown.getSelectionModel().select(DEFAULT_BOARD_SIZE);

        difficultyDropdown.getItems().setAll(Difficulty.values());
        difficultyDropdown.setValue(Difficulty.MEDIUM);


        playButton.setOnAction(event -> loadGameBoard());
    }

    /**
     * Loads the Sudoku game board with the selected settings.
     * Retrieves the chosen board size and difficulty from the dropdowns, initializes the game controller,
     * and replaces the current window with the game board.
     */
    private void loadGameBoard() {
        String selectedSize = sizeDropdown.getValue();
        int size = Integer.parseInt(selectedSize.split(SIZE_SEPARATOR)[0]);
        Difficulty difficulty = difficultyDropdown.getValue();

        String viewFilePath = Constants.PathConstants.SUDOKU_GAME_VIEW;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewFilePath));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            SudokuGameController controller = loader.getController();
            controller.initializeBoard(size, difficulty);

            stage.setTitle(Localization.get(I18N_TITLE_BOARD, size));
            stage.setScene(scene);
            stage.show();

            ((Stage) playButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
