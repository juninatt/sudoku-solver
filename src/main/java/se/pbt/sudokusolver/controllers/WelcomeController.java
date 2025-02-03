package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeController {

    @FXML
    private ComboBox<String> sizeDropdown;

    @FXML
    private Button playButton;

    @FXML
    public void initialize() {
        sizeDropdown.getItems().addAll("4x4", "6x6", "9x9", "12x12", "16x16", "25x25");
        sizeDropdown.getSelectionModel().select("9x9"); // Default selection

        playButton.setOnAction(event -> loadGameBoard());
    }

    private void loadGameBoard() {
        String selectedSize = sizeDropdown.getValue();
        int size = Integer.parseInt(selectedSize.split("x")[0]); // Extract size number

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/se/pbt/sudokusolver/sudoku-view.fxml"));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            SudokuController controller = loader.getController();
            controller.initBoard(size);

            stage.setTitle("Sudoku " + size + "x" + size);
            stage.setScene(scene);
            stage.show();

            ((Stage) playButton.getScene().getWindow()).close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
