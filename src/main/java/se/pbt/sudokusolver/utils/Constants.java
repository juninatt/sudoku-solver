package se.pbt.sudokusolver.utils;

import java.util.Set;

public class Constants {

    public static class ErrorMessages {

        public static final String BOARD_NOT_CREATED = "No board has been created yet!";
        public static final String INVALID_POSITION = "Row and column must be within board size";
        public static final String VALUE_MUST_BE = "Value must be between 0 and ";
        public static final String INVALID_BOARD_SIZE = "Invalid board size: {0}. Supported sizes: " + Constants.SudokuBoard.SUPPORTED_SIZES;
        public static final String INVALID_VALUE = "Value is not valid";
    }

    public static class FilePaths {

        public static final String WELCOME_VIEW = "/fxml/welcome-view.fxml";
        public static final String SUDOKU_VIEW = "/fxml/sudoku-view.fxml";
        public static final String MESSAGE_BUNDLE_SV = "i18n.messages_sv";
        public static final String MESSAGE_BUNDLE_EN = "i18n.messages_en";
        public static final String MESSAGE_BUNDLE_ES = "i18n.messages_es";
    }

    public static class CSS {

        public static final String SUBGRID = "subgrid";
        public static final String FILLED_CELL = "filled-cell";
        public static final String TEXT_FIELD = "text-field";
    }

    public static class SudokuBoard {

        public static final Set<Integer> SUPPORTED_SIZES = Set.of(4, 6, 9, 12, 16, 25);
        public static final String[] SIZE_OPTIONS = {"4x4", "6x6", "9x9", "12x12", "16x16", "25x25"};
        public static final String DEFAULT_SIZE = "9x9";

        public static int getSubgridSize(int size) {
            return switch (size) {
                case 4 -> 2;
                case 6 -> 3;
                case 9 -> 3;
                case 12 -> 4;
                case 16 -> 4;
                case 25 -> 5;
                default -> throw new IllegalArgumentException("Unsupported board size: " + size);
            };
        }

    }
}
