package se.pbt.sudokusolver.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.HashSet;
import java.util.stream.IntStream;

import static se.pbt.sudokusolver.shared.constants.SharedConstants.EMPTY_CELL;


/**
 * Validates {@link SudokuBoard} state when a game is finished.
 * Ensures each row, column, and sub-grid contains unique values and that the solution is valid.
 * Stateless and safe to reuse across boards: each validation pass reads its board directly
 * from method parameters rather than from stored instance state.
 */
public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    /**
     * Runs full Sudoku validation.
     */
    public boolean validateBoard(SudokuBoard board) {
        int rowLength = board.getRowLength();
        int[] subgridSize = board.getSubgridSize();

        boolean isValid = validateRows(board, rowLength)
                && validateColumns(board, rowLength)
                && validateSubgrids(board, rowLength, subgridSize);

        // TODO: Add user friendly game completion
        if (isValid) {
            logger.info("Sudoku board validated: VALID");
        } else {
            logger.warn("Sudoku board validated: INVALID");
        }

        return isValid;
    }


    /**
     * Checks the cell at {@code (row, col)} against Sudoku rules and reports which specific
     * constraint group(s) — row, column, and/or subgrid — it conflicts with. Like
     * {@link #validateCell}, this does not require the board to be full; it is intended for
     * checking a single move during gameplay rather than a finished puzzle.
     */
    public CellViolation checkCell(SudokuBoard board, int row, int col) {
        int rowLength = board.getRowLength();
        int[] subgridSize = board.getSubgridSize();

        int subgridStartRow = row - row % subgridSize[0];
        int subgridStartCol = col - col % subgridSize[1];

        CellViolation violation = new CellViolation(
                isValidRow(board, rowLength, row),
                isValidColumn(board, rowLength, col),
                isValidSubgrid(board, subgridStartRow, subgridStartCol, subgridSize[0], subgridSize[1])
        );

        if (!violation.isValid()) {
            logger.debug("Cell ({},{}) violates Sudoku rules: {}", row, col, violation);
        }

        return violation;
    }

    /**
     * Validates that the cell at {@code (row, col)} does not violate Sudoku rules,
     * i.e. that its value does not already appear elsewhere in the same row, column,
     * or subgrid. Unlike {@link #validateBoard}, this does not require the board to be full —
     * it is intended for checking a single move during gameplay rather than a finished puzzle.
     */
    public boolean validateCell(SudokuBoard board, int row, int col) {
        return checkCell(board, row, col).isValid();
    }

    /**
     * Reports which Sudoku constraint group(s) a single cell conflicts with.
     */
    public record CellViolation(boolean rowValid, boolean columnValid, boolean subgridValid) {
        public boolean isValid() {
            return rowValid && columnValid && subgridValid;
        }
    }


    /**
     * Validates that every row in the board contains only unique non-zero values.
     */
    private boolean validateRows(SudokuBoard board, int rowLength) {
        logger.debug("Validating rows for board size: {}", rowLength);

        boolean hasZeros = IntStream.range(0, rowLength)
                .flatMap(r -> IntStream.range(0, rowLength)
                        .map(c -> board.getCellValue(r, c)))
                .anyMatch(v -> v == EMPTY_CELL);

        if (hasZeros) {
            logger.debug("Row validation aborted — board contains 0-values, considered invalid");
            return false;
        }

        boolean valid = IntStream.range(0, rowLength)
                .allMatch(row -> isValidRow(board, rowLength, row));

        if (!valid) {
            logger.debug("Row validation failed — duplicate values found");
        }

        return valid;
    }


    /**
     * Validates that every column in the board contains only unique values.
     */
    private boolean validateColumns(SudokuBoard board, int rowLength) {
        logger.debug("Validating columns for board size: {}", rowLength);

        boolean valid = IntStream.range(0, rowLength)
                .allMatch(col -> isValidColumn(board, rowLength, col));

        if (!valid) {
            logger.debug("Column validation failed — duplicate values found");
        }

        return valid;
    }

    /**
     * Validates that all subgrids in the board contain only unique values.
     */
    private boolean validateSubgrids(SudokuBoard board, int rowLength, int[] subgridSize) {
        logger.debug("Validating subgrids for board size: {}, layout: {}x{}",
                rowLength, subgridSize[0], subgridSize[1]);

        int rows = subgridSize[0];
        int cols = subgridSize[1];

        boolean valid = true;

        for (int r = 0; r < rowLength; r += rows) {
            for (int c = 0; c < rowLength; c += cols) {
                if (!isValidSubgrid(board, r, c, rows, cols)) {
                    valid = false;
                }
            }
        }

        if (!valid) {
            logger.debug("Subgrid validation failed — duplicate values found");
        }

        return valid;
    }


    /**
     * Validates that a row contains only unique numbers, ignoring empty cells.
     * {@link #validateRows} additionally requires the whole board to be free of empty
     * cells before calling this; {@link #validateCell} does not make that requirement.
     */
    private boolean isValidRow(SudokuBoard board, int rowLength, int row) {
        return hasUniqueNumbers(
                IntStream.range(0, rowLength)
                        .map(col -> board.getCellValue(row, col))
        );
    }


    /**
     * Validates that a specific column contains only unique values.
     */
    private boolean isValidColumn(SudokuBoard board, int rowLength, int col) {
        return hasUniqueNumbers(IntStream.range(0, rowLength)
                .map(row -> board.getCellValue(row, col)));
    }

    /**
     * Validates that a specific subgrid contains only unique values.
     */
    private boolean isValidSubgrid(SudokuBoard board, int startRow, int startCol, int subgridRows, int subgridCols) {
        return hasUniqueNumbers(
                IntStream.range(0, subgridRows)
                        .flatMap(r -> IntStream.range(0, subgridCols)
                                .map(c -> board.getCellValue(startRow + r, startCol + c)))
        );
    }

    /**
     * Determines whether all non-zero values in the stream are unique.
     */
    private boolean hasUniqueNumbers(IntStream numberStream) {
        HashSet<Integer> seenNumbers = new HashSet<>();

        return numberStream
                .filter(num -> num != EMPTY_CELL)
                .allMatch(seenNumbers::add);
    }
}