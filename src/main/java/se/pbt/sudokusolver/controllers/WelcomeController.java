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
 * Manages UI text localization, dropdown selections, and transitions to the game board.
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
        welcomeLabel.setText(Localization.get("ui.welcome"));
        boardSizeLabel.setText(Localization.get("ui.label.boardSize"));
        difficultyLabel.setText(Localization.get("ui.label.difficulty"));
        playButton.setText(Localization.get("ui.button.play"));
        rulesButton.setText(Localization.get("ui.button.rules"));

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
        int size = Integer.parseInt(selectedSize.split("x")[0]); // Extract size number

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FilePaths.SUDOKU_VIEW));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            SudokuController controller = loader.getController();
            controller.initialize(size);

            stage.setTitle(Localization.get("ui.title.board", size));
            stage.setScene(scene);
            stage.show();

            ((Stage) playButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
