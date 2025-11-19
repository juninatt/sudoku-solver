package se.pbt.sudokusolver.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.shared.localization.Localization;

import java.io.IOException;

import static se.pbt.sudokusolver.shared.constants.Constants.PathConstants.MAIN_MENU_VIEW;
import static se.pbt.sudokusolver.shared.constants.Constants.UIConstants.I18N_TITLE_MAIN;

public class SudokuApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(SudokuApp.class);

    /**
     * Initializes and displays the application's primary window.
     * Ensures the main UI loads before user interaction begins.
     */

    @Override
    public void start(Stage primaryStage) throws IOException {
        logger.info("Launching SudokuApp");
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_VIEW));
        Scene scene = new Scene(loader.load());
        primaryStage.setScene(scene);
        primaryStage.setTitle(
                Localization.get(I18N_TITLE_MAIN));
        primaryStage.show();

        logger.info("SudokuApp UI loaded successfully");
    }

    /**
     * Starts the JavaFX runtime.
     * Used as the standard entry point for launching the application.
     */

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            logger.error("Fatal error during application launch", e);
            // TODO: Show user-friendly fallback dialog or at least a graceful exit
        }
    }
}
