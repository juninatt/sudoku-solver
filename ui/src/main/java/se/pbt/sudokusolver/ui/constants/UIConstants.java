package se.pbt.sudokusolver.ui.constants;

public class UIConstants {

    // Board size defaults
    public static final String[] BOARD_SIZE_OPTIONS = {"4x4", "6x6", "9x9"};
    public static final String DEFAULT_BOARD_SIZE = "9x9";


    // Paths
    public static final String MAIN_MENU_VIEW = "/fxml/main-menu-view.fxml";
    public static final String SUDOKU_GAME_VIEW = "/fxml/sudoku-game-view.fxml";
    public static final String RULES_VIEW = "/fxml/rules-view.fxml";


    // Functional cell values
    public static final int EMPTY_CELL = 0;
    public static final int MIN_CELL_VALUE = 1;
    public static final double CELL_SIZE = 40.0;


    //  CSS
    public static final String CSS_CLASS_SUBGRID = "subgrid";
    public static final String CSS_CLASS_FILLED_CELL = "filled-cell";


    // Localization
    // Values are set in ' resources/i18n/Resource Bundle 'messages' ' and connected to their UI parts in MainMenuController
    public static final String I18N_TITLE_MAIN = "ui.title.main";
    public static final String I18N_TITLE_BOARD = "ui.title.board";
    public static final String I18N_LABEL_WELCOME = "ui.welcome";
    public static final String I18N_LABEL_BOARD_SIZE = "ui.label.boardSize";
    public static final String I18N_LABEL_DIFFICULTY = "ui.label.difficulty";
    public static final String I18N_BUTTON_PLAY = "ui.button.play";
    public static final String I18N_BUTTON_RULES = "ui.button.rules";
    public static final String I18N_RULES_TITLE = "ui.rules.title";
    public static final String I18N_RULES_BODY = "ui.rules.body";
    public static final String I18N_CHECKBOX_CHEAT_MODE = "ui.checkbox.cheatMode";
    public static final String I18N_MENU_LANGUAGE = "ui.menu.language";
}
