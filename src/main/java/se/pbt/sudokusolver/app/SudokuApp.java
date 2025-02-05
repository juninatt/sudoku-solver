package se.pbt.sudokusolver.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.pbt.sudokusolver.utils.Constants;
import se.pbt.sudokusolver.utils.Localization;

import java.io.IOException;

public class SudokuApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FilePaths.WELCOME_VIEW));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle(Localization.get("ui.title.main"));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
