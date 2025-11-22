package se.pbt.sudokusolver.core.constants;

import java.util.Set;

public class CoreConstants {

    // Functional cell values
    public static final int EMPTY_CELL = 0;


    // Difficulty
    public static final double CLUE_FRACTION_EASY = 0.50;
    public static final double CLUE_FRACTION_MEDIUM = 0.35;
    public static final double CLUE_FRACTION_HARD = 0.20;

    public static final String I18N_DIFFICULTY_EASY = "ui.label.difficulty.easy";
    public static final String I18N_DIFFICULTY_MEDIUM = "ui.label.difficulty.medium";
    public static final String I18N_DIFFICULTY_HARD = "ui.label.difficulty.hard";


    // Sudoku Board
    public static final Set<Integer> SUPPORTED_BOARD_SIZES = Set.of(4, 6, 9);

    public static int[] getBlockLayout(int size) {
        return switch (size) {
            case 4 -> new int[]{2, 2};
            case 6 -> new int[]{2, 3};
            case 9 -> new int[]{3, 3};
            default -> throw new IllegalArgumentException("Unsupported board size: " + size);
        };
    }

    // Error messages
    public static final String ERROR_INVALID_BOARD_SIZE =
            "Invalid board size: {0}. Supported sizes: " + SUPPORTED_BOARD_SIZES;
}
