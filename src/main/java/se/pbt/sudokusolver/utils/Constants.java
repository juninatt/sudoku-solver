package se.pbt.sudokusolver.utils;

public class Constants {

    public static final String APP_NAME = "Sudoku Solver";

    public static class ErrorMessages {

        public static final String BOARD_NOT_CREATED = "No board has been created yet!";
        public static final String INVALID_POSITION = "Row and column must be within board size";
        public static final String VALUE_MUST_BE = "Value must be between 0 and ";
    }

    public static class FilePaths {

        public static final String WELCOME_VIEW = "/se/pbt/sudokusolver/fxml/welcome-view.fxml";
        public static final String SUDOKU_VIEW = "/se/pbt/sudokusolver/fxml/sudoku-view.fxml";
        public static final String MESSAGE_BUNDLE_SV = "se.pbt.sudokusolver.i18n.messages_sv";
    }

    public static class CSS {

        public static final String STYLE_SUBGRID = "-fx-border-color: black; -fx-border-width: 2;";
        public static final String STYLE_CELL = "-fx-font-size: 16;";
    }

    public static class SudokuBoard {

        public static final String[] ALL_SIZES = {"4x4", "6x6", "9x9", "12x12", "16x16", "25x25"};
        public static final String DEFAULT_SIZE = "9x9";
    }
}
