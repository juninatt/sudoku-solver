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

        public static int[] getBlockLayout(int size) {
            return switch (size) {
                case 4 -> new int[]{2, 2};   // 4x4 -> 2x2 subgrids (4 st)
                case 6 -> new int[]{2, 3};   // 6x6 -> 2x3 subgrids (6 st)
                case 9 -> new int[]{3, 3};   // 9x9 -> 3x3 subgrids (9 st)
                case 12 -> new int[]{3, 4};  // 12x12 -> 3x4 subgrids (12 st)
                case 16 -> new int[]{4, 4};  // 16x16 -> 4x4 subgrids (16 st)
                case 25 -> new int[]{5, 5};  // 25x25 -> 5x5 subgrids (25 st)
                default -> throw new IllegalArgumentException("Unsupported board size: " + size);
            };
        }
    }
}
