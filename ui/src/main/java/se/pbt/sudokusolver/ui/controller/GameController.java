package se.pbt.sudokusolver.ui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.game.service.GameService;
import se.pbt.sudokusolver.shared.game.PuzzleDifficulty;
import se.pbt.sudokusolver.shared.localization.Localization;
import se.pbt.sudokusolver.ui.interaction.CellEventHandler;
import se.pbt.sudokusolver.ui.view.BoardGrid;
import se.pbt.sudokusolver.ui.view.CellFactory;
import se.pbt.sudokusolver.ui.viewmodel.SudokuViewModel;
import se.pbt.sudokusolver.validation.Validator;

import java.io.IOException;

import static se.pbt.sudokusolver.ui.constants.UIConstants.I18N_TITLE_MAIN;
import static se.pbt.sudokusolver.ui.constants.UIConstants.MAIN_MENU_VIEW;

/**
 * Acts as the main UI controller for the Sudoku game screen.
 * Initializes UI components, constructs the ViewModel and underlying GameService,
 * and connects user actions to game logic. Responsible for rendering the board,
 * handling navigation, and enabling optional cheat features.
 */
public class GameController {
    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @FXML private GridPane gridPane;
    @FXML private Button homeButton;
    @FXML private HBox cheatButtonsBox;
    @FXML private Button nextHintButton;
    @FXML private Button solveBoardButton;

    private SudokuViewModel viewModel;

    /**
     * Initializes static UI elements defined in the FXML, including the home button
     * and the initial hidden state of cheat buttons.
     * Called automatically by JavaFX after the FXML layout has been loaded.
     */
    @FXML
    public void initialize() {
        logger.debug("GameController initialized");

        FontIcon homeIcon = new FontIcon(FontAwesomeSolid.HOME);
        homeIcon.setIconSize(20);
        homeButton.setGraphic(homeIcon);
        homeButton.setOnAction(event -> returnToMainMenu());

        cheatButtonsBox.setVisible(false);
        cheatButtonsBox.setManaged(false);
    }

    /**
     * Builds and displays a new Sudoku board using the specified board size and difficulty.
     * Creates all dependencies required by the ViewModel and connects the UI to the
     * GameService through a {@link BoardGrid} cell listener.
     */
    public void initializeBoard(int size, PuzzleDifficulty difficulty) {
        logger.info("Initializing board (size: {}, difficulty: {})", size, difficulty);

        GameService gameService = createGameService();
        viewModel = createViewModel(gameService);

        viewModel.createSudokuGame(size, difficulty);

        BoardGrid boardGrid = new BoardGrid(size, viewModel);
        boardGrid.setupGrid();

        gameService.setCellViewListener(boardGrid);

        gridPane.getChildren().setAll(boardGrid.getGridPane());

        logger.debug("BoardGrid created and added to UI");
    }

    /**
     * Creates a new {@link GameService} instance along with its required {@link Validator}.
     */
    private GameService createGameService() {
        return new GameService(new Validator());
    }

    /**
     * Creates a fully constructed {@link SudokuViewModel} using the given GameService.
     */
    private SudokuViewModel createViewModel(GameService gameService) {
        return new SudokuViewModel(
                gameService,
                new CellEventHandler(),
                new CellFactory()
        );
    }

    /**
     * Returns the user to the application's main menu by loading the menu FXML and closing the current window.
     */
    private void returnToMainMenu() {
        logger.info("Returning to main menu");

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MAIN_MENU_VIEW));
            Stage stage = new Stage();
            stage.setTitle(Localization.get(I18N_TITLE_MAIN));
            stage.setScene(new Scene(loader.load()));
            stage.show();

            ((Stage) homeButton.getScene().getWindow()).close();

        } catch (IOException e) {
            logger.error("Failed to load main menu view", e);
        }
    }

    /**
     * Activates cheat mode when set to true by enabling reveal and solve controls.
     */
    public void setCheatMode(boolean cheatModeEnabled) {
        if (cheatModeEnabled) {
            enableCheatButtons();
        }
    }

    /**
     * Enables UI controls that reveal or auto-solve puzzle values.
     */
    private void enableCheatButtons() {
        logger.debug("Cheat buttons enabled");

        cheatButtonsBox.setVisible(true);
        cheatButtonsBox.setManaged(true);

        nextHintButton.setOnAction(event -> viewModel.revealCellFromSolution());
        solveBoardButton.setOnAction(event -> viewModel.revealAllCellsFromSolution());
    }
}
