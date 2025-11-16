package se.pbt.sudokusolver.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import se.pbt.sudokusolver.utils.Localization;

import static se.pbt.sudokusolver.utils.Constants.UIConstants.*;

/**
 * Controller for the rules view.
 * Loads and updates the localized rule title and rule text when the view is initialized
 * or when the active language changes.
 */
public class RulesController {

    @FXML private Label titleLabel;
    @FXML private TextArea bodyArea;

    /**
     * Initializes the view by applying the current localized text.
     */
    @FXML
    public void initialize() {
        updateTexts();
    }

    /**
     * Updates the rule title and body using the currently active localization bundle.
     */
    public void updateTexts() {
        titleLabel.setText(Localization.get(I18N_RULES_TITLE));
        bodyArea.setText(Localization.get(I18N_RULES_BODY));
    }
}
