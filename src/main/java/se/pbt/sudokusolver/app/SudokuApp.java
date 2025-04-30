package se.pbt.sudokusolver.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.pbt.sudokusolver.utils.Localization;

import java.io.IOException;

import static se.pbt.sudokusolver.utils.Constants.PathConstants.MAIN_MENU_VIEW;
import static se.pbt.sudokusolver.utils.Constants.UIConstants.I18N_TITLE_MAIN;

public class SudokuApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_VIEW));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle(Localization.get(I18N_TITLE_MAIN));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
