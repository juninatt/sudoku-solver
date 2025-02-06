package se.pbt.sudokusolver.utils;

import java.util.Set;

public class Constants {

    public static class ErrorMessages {

        public static final String BOARD_NOT_CREATED = "No board has been created yet!";
        public static final String INVALID_POSITION = "Row and column must be within board size";
        public static final String VALUE_MUST_BE = "Value must be between 0 and ";
        public static final String INVALID_BOARD_SIZE = "Invalid board size: {0}. Supported sizes: " + Constants.SudokuBoard.SUPPORTED_SIZES;

    }

    public static class FilePaths {

        public static final String WELCOME_VIEW = "/fxml/welcome-view.fxml";
        public static final String SUDOKU_VIEW = "/fxml/sudoku-view.fxml";
        public static final String MESSAGE_BUNDLE_SV = "i18n.messages_sv";
        public static final String MESSAGE_BUNDLE_EN = "i18n.messages_en";
        public static final String MESSAGE_BUNDLE_ES = "i18n.messages_es";
    }

    public static class CSS {

        public static final String STYLE_SUBGRID = "-fx-border-color: black; -fx-border-width: 2;";
        public static final String STYLE_CELL = "-fx-font-size: 16;";
    }

    public static class SudokuBoard {

        public static final Set<Integer> SUPPORTED_SIZES = Set.of(4, 6, 9, 12, 16, 25);
        public static final String[] SIZE_OPTIONS = {"4x4", "6x6", "9x9", "12x12", "16x16", "25x25"};
        public static final String DEFAULT_SIZE = "9x9";

        public static int[] getBlockLayout(int size) {
            return switch (size) {
                case 4 -> new int[]{2, 2};
                case 6 -> new int[]{2, 3};
                case 9 -> new int[]{3, 3};
                case 12 -> new int[]{3, 4};
                case 16 -> new int[]{4, 4};
                case 25 -> new int[]{5, 5};
                default -> throw new IllegalArgumentException("Unsupported board size: " + size);
            };
        }
    }
}
