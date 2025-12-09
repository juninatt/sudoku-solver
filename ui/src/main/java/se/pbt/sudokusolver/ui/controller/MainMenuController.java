package se.pbt.sudokusolver.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.game.model.Difficulty;
import se.pbt.sudokusolver.shared.game.PuzzleDifficulty;
import se.pbt.sudokusolver.shared.localization.Localization;

import java.io.IOException;
import java.util.Locale;

import static se.pbt.sudokusolver.ui.constants.UIConstants.*;

/**
 * Controller for the main menu view.
 * Handles initialization of dropdowns, localization updates, language switching,
 * and navigation to the Sudoku game view. Also controls cheat-mode options.
 */
public class MainMenuController {

    private static final Logger logger = LoggerFactory.getLogger(MainMenuController.class);

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
     * Initializes UI components after FXML loading.
     * Populates dropdowns, applies localized text, and wires button actions.
     */
    @FXML
    public void initialize() {
        logger.debug("Initializing main menu UI");

        initializeDropdowns();
        updateLocalizedTexts();

        playButton.setOnAction(event -> loadGameBoard());
    }

    /**
     * Refreshes UI text after a locale change.
     * Re-applies localized labels and repopulates dropdowns.
     */
    public void updateTexts() {
        logger.debug("Updating localized UI texts");
        updateLocalizedTexts();
        initializeDropdowns();
    }

    /**
     * Updates labels, buttons, and menu titles based on active localization settings.
     */
    private void updateLocalizedTexts() {
        welcomeLabel.setText(Localization.get(I18N_LABEL_WELCOME));
        boardSizeLabel.setText(Localization.get(I18N_LABEL_BOARD_SIZE));
        difficultyLabel.setText(Localization.get(I18N_LABEL_DIFFICULTY));
        cheatModeCheckbox.setText(Localization.get(I18N_CHECKBOX_CHEAT_MODE));
        playButton.setText(Localization.get(I18N_BUTTON_PLAY));
        rulesButton.setText(Localization.get(I18N_BUTTON_RULES));
        languageMenu.setText(Localization.get(I18N_MENU_LANGUAGE));
    }

    /**
     * Populates dropdowns for board size selection and difficulty selection.
     * Uses predefined UI constants for available options.
     */
    private void initializeDropdowns() {
        sizeDropdown.getItems().setAll(BOARD_SIZE_OPTIONS);
        sizeDropdown.getSelectionModel().select(DEFAULT_BOARD_SIZE);

        difficultyDropdown.getItems().setAll(Difficulty.values());
        difficultyDropdown.setValue(Difficulty.MEDIUM);
    }

    /**
     * Loads the game board view using the selected size and difficulty.
     * Initializes the game controller and closes the main menu window.
     */
    private void loadGameBoard() {
        logger.info("User requested to start a new game");

        if (sizeDropdown.getValue() == null || difficultyDropdown.getValue() == null) {
            logger.warn("Game start requested without selecting size or difficulty");
            return;
        }

        boolean cheatModeEnabled = cheatModeCheckbox.isSelected();
        int size = Integer.parseInt(sizeDropdown.getValue().split("x")[0]);
        PuzzleDifficulty difficulty = difficultyDropdown.getValue();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(SUDOKU_GAME_VIEW));
            Stage stage = new Stage();
            Scene scene = new Scene(loader.load());

            GameController controller = loader.getController();
            controller.setCheatMode(cheatModeEnabled);
            controller.initializeBoard(size, difficulty);

            stage.setTitle(Localization.get(I18N_TITLE_BOARD, size));
            stage.setScene(scene);
            stage.show();

            logger.info("Game board loaded successfully (size={}, difficulty={})", size, difficulty);
            ((Stage) playButton.getScene().getWindow()).close();

        } catch (IOException e) {
            logger.error("Failed to load game board view", e);
        }
    }

    /**
     * Changes application language based on user menu selection,
     * updates all UI text and resizes the window if needed.
     */
    @FXML
    public void changeLanguage(javafx.event.ActionEvent event) {
        String languageCode = ((MenuItem) event.getSource()).getId();

        logger.info("Switching language to '{}'", languageCode);

        Localization.setLocale(new Locale(languageCode));
        updateTexts();

        Stage stage = (Stage) welcomeLabel.getScene().getWindow();
        stage.sizeToScene();
    }

    /**
     * Opens the rules dialog window, loads localized rules content and displays it in a separate stage.
     */
    @FXML
    private void onRulesClicked() {
        logger.debug("Opening rules window");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(RULES_VIEW));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle(Localization.get(I18N_RULES_TITLE));

            RulesController controller = loader.getController();
            controller.updateTexts();

            stage.show();

        } catch (IOException e) {
            logger.error("Failed to load rules view", e);
        }
    }
}
