package se.pbt.sudokusolver.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.utils.Localization;

import java.io.IOException;

import static se.pbt.sudokusolver.utils.Constants.PathConstants.MAIN_MENU_VIEW;
import static se.pbt.sudokusolver.utils.Constants.UIConstants.I18N_TITLE_MAIN;

public class SudokuApp extends Application {
    private static final Logger logger = LoggerFactory.getLogger(SudokuApp.class);

    /**
     * Loads the main menu view from its FXML definition and wraps it in a Scene.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {

        logger.info("Launching SudokuApp");

        Scene scene = buildMainScene();
        primaryStage.setScene(scene);
        primaryStage.setTitle(Localization.get(I18N_TITLE_MAIN));
        primaryStage.show();

        logger.info("SudokuApp UI loaded successfully");
    }

    /**
     * Entry point of the application. Launches the JavaFX runtime.
     */
    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            logger.error("Fatal error during application launch", e);
            // TODO: Show user-friendly fallback dialog or at least a graceful exit
        }
    }

    /**
     * Loads and builds the main menu scene from the FXML file.
     */
    private Scene buildMainScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_VIEW));
        return new Scene(loader.load());
    }
}
