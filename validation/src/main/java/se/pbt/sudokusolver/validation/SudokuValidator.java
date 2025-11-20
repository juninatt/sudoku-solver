package se.pbt.sudokusolver.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.HashSet;
import java.util.stream.IntStream;

import static se.pbt.sudokusolver.validation.ValidationConstants.EMPTY_CELL;


/**
 * Validates a {@link SudokuBoard} by ensuring that all rows, columns, and subgrids
 * contain only unique non-zero values according to standard Sudoku rules.
 * This class operates in a read-only manner and does not modify the board.
 */
public class SudokuValidator {
    private static final Logger logger = LoggerFactory.getLogger(SudokuValidator.class);

    private final SudokuBoard board;
    private final int boardSize;
    private final int[] subgridDimensions;

    /**
     * Sets up a read-only validator for the given Sudoku board.
     */
    public SudokuValidator(SudokuBoard board) {
        this.board = board;
        this.boardSize = board.getSize();
        this.subgridDimensions = board.getSubgridDimensions();
    }

    /**
     * Validates the entire board by checking rows, columns, and subgrids.
     * Acts as the main entry point for rule verification.
     */
    public boolean validateBoard() {
        boolean isValid = validateRows() && validateColumns() && validateSubgrids();

        // TODO: Add user friendly game completion
        if (isValid) {
            logger.info("Sudoku board validated: valid");
        } else {
            logger.warn("Sudoku board validated: invalid");
        }

        return isValid;
    }

    //  Row, Column, and Subgrid Validation

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

    //  Unit Validation

    /**
     * Validates that a specific row contains only unique non-zero values.
     */
    private boolean isValidRow(int row) {
        boolean valid = hasUniqueNumbers(IntStream.range(0, boardSize)
                .map(col -> board.getValueAt(row, col)));

        if (!valid) {
            logger.debug("Invalid row detected at index {}", row);
        }

        return valid;
    }

    /**
     * Validates that a specific column contains only unique non-zero values.
     */
    private boolean isValidColumn(int col) {
        boolean valid = hasUniqueNumbers(IntStream.range(0, boardSize)
                .map(row -> board.getValueAt(row, col)));

        if (!valid) {
            logger.debug("Invalid column detected at index {}", col);
        }

        return valid;
    }


    /**
     * Validates that a subgrid (block) contains only unique non-zero values.
     */
    private boolean isValidSubgrid(int startRow, int startCol, int subgridRows, int subgridCols) {
        boolean valid = hasUniqueNumbers(
                IntStream.range(0, subgridRows)
                        .flatMap(r -> IntStream.range(0, subgridCols)
                                .map(c -> board.getValueAt(startRow + r, startCol + c)))
        );

        if (!valid) {
            logger.debug(
                    "Invalid subgrid detected at start position ({}, {}) with size {}x{}",
                    startRow, startCol, subgridRows, subgridCols
            );
        }

        return valid;
    }

    /**
     * Determines whether all non-zero values in the stream are unique.
     * Used as the fundamental check for rows, columns, and subgrids.
     */
    private boolean hasUniqueNumbers(IntStream numberStream) {
        HashSet<Integer> seenNumbers = new HashSet<>();
        return numberStream
                .filter(num -> num != EMPTY_CELL)
                .allMatch(seenNumbers::add);
    }
}
