package se.pbt.sudokusolver.utils;

import se.pbt.sudokusolver.models.SudokuBoard;

import java.util.HashSet;
import java.util.stream.IntStream;

/**
 * Validates a {@link SudokuBoard} by ensuring that all rows, columns, and subgrids
 * contain only unique non-zero values according to standard Sudoku rules.
 * This class operates in a read-only manner and does not modify the board.
 */
public class SudokuValidator {

    private final SudokuBoard board;
    private final int boardSize;
    private final int[] subgridDimensions;

    /**
     * Constructs a validator for the given Sudoku board.
     *
     * @param board the board to validate
     */
    public SudokuValidator(SudokuBoard board) {
        this.board = board;
        this.boardSize = board.getSize();
        this.subgridDimensions = board.getSubgridDimensions();
    }

    /**
     * Checks if the board is valid according to Sudoku rules.
     * All rows, columns, and subgrids must contain unique non-zero values.
     *
     * @return {@code true} if the board is valid, otherwise {@code false}
     */
    public boolean validateBoard() {
        boolean isValid = validateRows() && validateColumns() && validateSubgrids();

        if (isValid) {
            System.out.println("✅ Sudoku board is valid! (WINNER)");
        } else {
            System.out.println("❌ Sudoku board is invalid! (LOSER)");
        }

        return isValid;
    }

    // ---------- Row, Column, and Subgrid Validation ----------

    /**
     * Validates that every row in the board contains only unique non-zero values.
     */
    private boolean validateRows() {
        return IntStream.range(0, boardSize)
                .allMatch(this::isValidRow);
    }

    /**
     * Validates that every column in the board contains only unique non-zero values.
     */
    private boolean validateColumns() {
        return IntStream.range(0, boardSize)
                .allMatch(this::isValidColumn);
    }

    /**
     * Validates that all subgrids (blocks) in the board contain only unique non-zero values.
     */
    private boolean validateSubgrids() {
        int subgridRows = subgridDimensions[0];
        int subgridCols = subgridDimensions[1];

        for (int row = 0; row < boardSize; row += subgridRows) {
            for (int col = 0; col < boardSize; col += subgridCols) {
                if (!isValidSubgrid(row, col, subgridRows, subgridCols)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ---------- Unit Validation ----------

    /**
     * Validates that a specific row contains only unique non-zero values.
     */
    private boolean isValidRow(int row) {
        return hasUniqueNumbers(IntStream.range(0, boardSize)
                .map(col -> board.getValueAt(row, col)));
    }

    /**
     * Validates that a specific column contains only unique non-zero values.
     */
    private boolean isValidColumn(int col) {
        return hasUniqueNumbers(IntStream.range(0, boardSize)
                .map(row -> board.getValueAt(row, col)));
    }

    /**
     * Validates that a subgrid (block) contains only unique non-zero values.
     */
    private boolean isValidSubgrid(int startRow, int startCol, int subgridRows, int subgridCols) {
        return hasUniqueNumbers(
                IntStream.range(0, subgridRows)
                        .flatMap(r -> IntStream.range(0, subgridCols)
                                .map(c -> board.getValueAt(startRow + r, startCol + c)))
        );
    }

    /**
     * Helper method that returns true if all non-zero numbers in the stream are unique.
     */
    private boolean hasUniqueNumbers(IntStream numberStream) {
        HashSet<Integer> seenNumbers = new HashSet<>();
        return numberStream
                .filter(num -> num != 0)
                .allMatch(seenNumbers::add);
    }
}
