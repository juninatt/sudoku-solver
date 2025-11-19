package se.pbt.sudokusolver.shared.constants;

import java.util.Set;

/**
 * Central container class for shared constants across the application.
 */
public class Constants {

    /**
     * Contains core logic constants related to the Sudoku board, such as supported sizes,
     * value limits, and layout calculation logic.
     */
    public static final class GameConstants {

        // Board Size Definitions
        public static final Set<Integer> SUPPORTED_BOARD_SIZES = Set.of(4, 6, 9);
        public static final String[] BOARD_SIZE_OPTIONS = {"4x4", "6x6", "9x9"};
        public static final String DEFAULT_BOARD_SIZE = "9x9";
        public static final String SIZE_SEPARATOR = "x";

        // Board Cell Constraints
        public static final int EMPTY_CELL = 0;
        public static final int FIRST_INDEX = 0;
        public static final int MIN_VALID_CELL_VALUE = 1;

        // Error Messages
        public static final String ERROR_INVALID_BOARD_SIZE =
                "Invalid board size: {0}. Supported sizes: " + SUPPORTED_BOARD_SIZES;
        public static final String ERROR_BOARD_GENERATION_FAILED =
                "Unable to generate a complete Sudoku solution. Invalid puzzle state.";

        /**
         * Determines subgrid layout (rows x columns) for a given board size.
         */
        public static int[] getBlockLayout(int size) {
            return switch (size) {
                case 4 -> new int[]{2, 2};   // 2x2 blocks
                case 6 -> new int[]{2, 3};   // 2x3 blocks
                case 9 -> new int[]{3, 3};   // 3x3 blocks
                default -> throw new IllegalArgumentException("Unsupported board size: " + size);
            };
        }

        private GameConstants() {}
    }


    /**
     * Contains file paths to FXML views and resource bundles used in the UI.
     * Used to centralize access to all static resource paths in the application.
     */
    public static final class PathConstants {

        // FXML View Paths
        public static final String MAIN_MENU_VIEW = "/fxml/main-menu-view.fxml";
        public static final String RULES_VIEW = "/fxml/rules-view.fxml";
        public static final String SUDOKU_GAME_VIEW = "/fxml/sudoku-game-view.fxml";


        // Localization Bundles
        public static final String BUNDLE_SV = "i18n.messages_sv";
        public static final String BUNDLE_EN = "i18n.messages_en";
        public static final String BUNDLE_ES = "i18n.messages_es";
        public static final String BUNDLE_CA = "i18n.messages_ca";
        public static final String BUNDLE_DE = "i18n.messages_de";
        public static final String BUNDLE_FR = "i18n.messages_fr";

        private PathConstants() {}
    }


    /**
     * Contains UI-related constants such as layout dimensions, CSS styles,
     * gameplay rules and localization keys.
     */
    public static final class UIConstants {

        // Layout
        public static final double CELL_SIZE = 40.0;
        public static final int FIRST_ROW_INDEX = 0;
        public static final int FIRST_COLUMN_INDEX = 0;

        // Gameplay rules
        public static final int MAX_ALLOWED_SOLUTIONS = 1;
        public static final int MIN_CELL_VALUE = 1;

        // Clue fractions for different difficulties
        public static final double CLUE_FRACTION_EASY = 0.50;
        public static final double CLUE_FRACTION_MEDIUM = 0.35;
        public static final double CLUE_FRACTION_HARD = 0.20;

        // CSS style classes
        public static final String CSS_CLASS_SUBGRID = "subgrid";
        public static final String CSS_CLASS_FILLED_CELL = "filled-cell";

        // Localization keys – general UI
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

        // Localization keys – difficulty
        public static final String I18N_DIFFICULTY_EASY = "ui.label.difficulty.easy";
        public static final String I18N_DIFFICULTY_MEDIUM = "ui.label.difficulty.medium";
        public static final String I18N_DIFFICULTY_HARD = "ui.label.difficulty.hard";

        private UIConstants() {}
    }
}
