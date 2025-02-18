package se.pbt.sudokusolver.models;

import java.util.HashSet;
import java.util.stream.IntStream;

/**
 * Validates a {@link SudokuBoard} by ensuring that all rows, columns, and subgrids contain unique numbers.
 * This class does not modify the board but provides methods to check the validity of the current state.
 */
public class SudokuValidator {

    private final SudokuBoard board;
    private final int boardSize;
    private final int subgridSize;

    /**
     * Initializes the validator with a reference to a Sudoku board.
     * Retrieves the board size and subgrid size to optimize validation operations.
     *
     * @param board The Sudoku board to be validated.
     */
    public SudokuValidator(SudokuBoard board) {
        this.board = board;
        this.boardSize = board.getBoardSize();
        this.subgridSize = board.getSubgridSize();
    }

    /**
     * Validates the entire Sudoku board by checking all rows, columns, and subgrids.
     *
     * @return {@code true} if the board follows Sudoku rules, otherwise {@code false}.
     */
    public boolean validateBoard() {
        boolean isValid = validateRows() && validateColumns() && validateSubgrids();

        // Print validation result for debugging
        if (isValid) {
            System.out.println("✅ Sudoku board is valid! (WINNER)");
        } else {
            System.out.println("❌ Sudoku board is invalid! (LOSER)");
        }

        return isValid;
    }


    /**
     * Validates all rows to ensure each contains unique numbers.
     */
    private boolean validateRows() {
        return IntStream.range(0, boardSize)
                .allMatch(this::isValidRow);
    }

    /**
     * Validates all columns to ensure each contains unique numbers.
     */
    private boolean validateColumns() {
        return IntStream.range(0, boardSize)
                .allMatch(this::isValidColumn);
    }

    /**
     * Validates all subgrids to ensure each contains unique numbers.
     */
    private boolean validateSubgrids() {
        for (int row = 0; row < boardSize; row += subgridSize) {
            for (int col = 0; col < boardSize; col += subgridSize) {
                if (!isValidSubgrid(row, col)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a given row contains unique numbers.
     */
    private boolean isValidRow(int row) {
        return hasUniqueNumbers(IntStream.range(0, boardSize)
                .map(col -> board.getValueAt(row, col)));
    }

    /**
     * Checks if a given column contains unique numbers.
     */
    private boolean isValidColumn(int col) {
        return hasUniqueNumbers(IntStream.range(0, boardSize)
                .map(row -> board.getValueAt(row, col)));
    }

    /**
     * Checks if a given subgrid contains unique numbers.
     */
    private boolean isValidSubgrid(int startRow, int startCol) {
        return hasUniqueNumbers(
                IntStream.range(0, subgridSize)
                        .flatMap(row -> IntStream.range(0, subgridSize)
                                .map(col -> board.getValueAt(startRow + row, startCol + col)))
        );
    }

    /**
     * Helper method to check if a sequence of numbers contains only unique values.
     */
    private boolean hasUniqueNumbers(IntStream numberStream) {
        HashSet<Integer> seenNumbers = new HashSet<>();
        return numberStream
                .filter(num -> num != 0) // Ignore empty cells
                .allMatch(seenNumbers::add);
    }

    /**
     * Checks if a given number is within the valid range for the board.
     */
    public boolean isOutOfBounds(int value) {
        return value < 1 || value > boardSize;
    }

    /**
     * Checks if a given row and column position is within the board's valid range.
     */
    public boolean isOutOfBounds(int row, int col) {
        return row < 0 || row >= boardSize || col < 0 || col >= boardSize;
    }
}
