package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import se.pbt.sudokusolver.models.Difficulty;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.Localization;

import java.io.IOException;
import java.util.Locale;

import static se.pbt.sudokusolver.utils.Constants.GameConstants.*;
import static se.pbt.sudokusolver.utils.Constants.UIConstants.*;

/**
 * Controller for the main menu view.
 * Handles initialization of dropdowns, language switching, and navigation to the Sudoku board.
 * Also manages UI updates when changing languages dynamically during runtime.
 */
public class MainMenuController {

    @FXML private Label welcomeLabel;
    @FXML private Label boardSizeLabel;
    @FXML private Label difficultyLabel;
    @FXML private ComboBox<Difficulty> difficultyDropdown;
    @FXML private ComboBox<String> sizeDropdown;
    @FXML private CheckBox cheatModeCheckbox;
    @FXML private Button playButton;
    @FXML private Button rulesButton;
    @FXML private Menu languageMenu;

    /**
     * Initializes the menu with default values and localized text.
     */
    @FXML
    public void initialize() {
        initializeDropdowns();
        updateLocalizedTexts();
        playButton.setOnAction(event -> loadGameBoard());
    }

    /**
     * Called after a locale change to update all visible text elements.
     */
    public void updateTexts() {
        updateLocalizedTexts();
        initializeDropdowns();
    }

    /**
     * Updates labels, buttons, and checkbox with localized strings.
     */
    private void updateLocalizedTexts() {
        welcomeLabel.setText(Localization.get(I18N_LABEL_WELCOME));
        boardSizeLabel.setText(Localization.get(I18N_LABEL_BOARD_SIZE));
        difficultyLabel.setText(Localization.get(I18N_LABEL_DIFFICULTY));
        cheatModeCheckbox.setText(Localization.get(I18N_CHECKBOX_CHEAT_MODE));
        playButton.setText(Localization.get(I18N_BUTTON_PLAY));
        rulesButton.setText(Localization.get(I18N_BUTTON_RULES));
        rulesButton.setText(Localization.get(I18N_BUTTON_RULES));
        languageMenu.setText(Localization.get(I18N_MENU_LANGUAGE));
    }

    /**
     * Populates dropdowns for board size and difficulty.
     */
    private void initializeDropdowns() {
        sizeDropdown.getItems().setAll(BOARD_SIZE_OPTIONS);
        sizeDropdown.getSelectionModel().select(DEFAULT_BOARD_SIZE);

        difficultyDropdown.getItems().setAll(Difficulty.values());
        difficultyDropdown.setValue(Difficulty.MEDIUM);
    }

    /**
     * Loads the game board view with user-selected parameters.
     * Closes the menu window after launching the game.
     */
    private void loadGameBoard() {
        if (sizeDropdown.getValue() == null || difficultyDropdown.getValue() == null) {
            // TODO: Replace with user-friendly validation
            System.out.println("Please select a board size and difficulty before starting the game.");
            return;
        }
        boolean cheatModeEnabled = cheatModeCheckbox.isSelected();
        String selectedSize = sizeDropdown.getValue();
        int size = Integer.parseInt(selectedSize.split(SIZE_SEPARATOR)[0]);
        Difficulty difficulty = difficultyDropdown.getValue();

        String viewFilePath = Constants.PathConstants.SUDOKU_GAME_VIEW;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(viewFilePath));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            SudokuGameController controller = loader.getController();
            controller.setCheatMode(cheatModeEnabled);
            controller.initializeBoard(size, difficulty);

            stage.setTitle(Localization.get(I18N_TITLE_BOARD, size));
            stage.setScene(scene);
            stage.show();

            ((Stage) playButton.getScene().getWindow()).close();
        } catch (IOException e) {
            // TODO: Replace with proper logging instead of printing to console
            System.out.println(e.getMessage());
        }
    }

    /**
     * Called when the user selects a language from the menu.
     * Updates the UI to reflect the new language and adjusts window size if needed.
     *
     * @param event The ActionEvent triggered by selecting a language item.
     */
    @FXML
    public void changeLanguage(javafx.event.ActionEvent event) {
        String languageCode = ((MenuItem) event.getSource()).getId();
        Localization.setLocale(new Locale(languageCode));
        updateTexts();


        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.sizeToScene(); // Resize window if translated text needs more space
    }

    /**
     * Opens the localized rules window.
     * Loads the rules, applies the current language, and displays the view in a separate stage.
     */
    @FXML
    private void onRulesClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.PathConstants.RULES_VIEW));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(Localization.get(I18N_RULES_TITLE));

            RulesController controller = loader.getController();
            controller.updateTexts();

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
