package se.pbt.sudokusolver;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;

public class WelcomeController {

    @FXML
    private ComboBox<String> sizeDropdown;

    @FXML
    private ComboBox<String> difficultyDropdown;

    @FXML
    private Button playButton;

    @FXML
    private Button rulesButton;

    @FXML
    public void initialize() {
        sizeDropdown.setItems(FXCollections.observableArrayList("4x4", "6x6", "9x9", "12x12", "16x16", "25x25"));
        sizeDropdown.getSelectionModel().select("9x9"); // Default option

        difficultyDropdown.setItems(FXCollections.observableArrayList("Enkel", "Medel", "Svår", "Expert"));
        difficultyDropdown.getSelectionModel().select("Medel"); // Default option

        playButton.setOnAction(event -> startGame());

        rulesButton.setOnAction(event -> showRules());
    }

    private void startGame() {
        String selectedSize = sizeDropdown.getValue();
        String selectedDifficulty = difficultyDropdown.getValue();

        System.out.println("Startar spelet med storlek: " + selectedSize + ", svårighetsgrad: " + selectedDifficulty);

    }

    private void showRules() {
        System.out.println("Visar regler för Sudoku");
    }
}
