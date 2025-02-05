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

    @FXML
    public void initialize() {
        welcomeLabel.setText(Localization.get("ui.welcome"));
        boardSizeLabel.setText(Localization.get("ui.label.boardSize"));
        difficultyLabel.setText(Localization.get("ui.label.difficulty"));
        playButton.setText(Localization.get("ui.button.play"));
        rulesButton.setText(Localization.get("ui.button.rules"));

        sizeDropdown.getItems().addAll(Constants.SudokuBoard.ALL_SIZES);
        sizeDropdown.getSelectionModel().select(Constants.SudokuBoard.DEFAULT_SIZE);

        playButton.setOnAction(event -> loadGameBoard());
    }

    private void loadGameBoard() {
        String selectedSize = sizeDropdown.getValue();
        int size = Integer.parseInt(selectedSize.split("x")[0]); // Extract size number

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FilePaths.SUDOKU_VIEW));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            SudokuController controller = loader.getController();
            controller.initBoard(size);

            stage.setTitle(Localization.get("ui.title.board", size));
            stage.setScene(scene);
            stage.show();

            ((Stage) playButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
