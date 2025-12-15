package se.pbt.sudokusolver.validation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.HashSet;
import java.util.stream.IntStream;

import static se.pbt.sudokusolver.validation.ValidationConstants.EMPTY_CELL;


/**
 * Validates {@link SudokuBoard} state when a game is finished.
 * Ensures each row, column, and sub-grid contains unique values and that the solution is valid.
 */
public class Validator {
    private static final Logger logger = LoggerFactory.getLogger(Validator.class);

    private  SudokuBoard sudokuBoard;
    private  int rowLength;
    private  int[] subgridSize;


    public Validator() {}

    /**
     * Runs full Sudoku validation.
     * Loads boardSize + subgridDimensions from the given board so later checks operate on correct bounds.
     */
    public boolean validateBoard(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
        this.rowLength = sudokuBoard.getRowLength();
        this.subgridSize = sudokuBoard.getSubgridSize();

        boolean isValid = validateRows() && validateColumns() && validateSubgrids();

        // TODO: Add user friendly game completion
        if (isValid) {
            logger.info("Sudoku board validated: VALID");
        } else {
            logger.warn("Sudoku board validated: INVALID");
        }

        return isValid;
    }


    /**
     * Validates that every row in the board contains only unique non-zero values.
     */
    private boolean validateRows() {
        logger.debug("Validating rows for board size: {}", rowLength);

        boolean hasZeros = IntStream.range(0, rowLength)
                .flatMap(r -> IntStream.range(0, rowLength)
                        .map(c -> sudokuBoard.getCellValue(r, c)))
                .anyMatch(v -> v == EMPTY_CELL);

        if (hasZeros) {
            logger.debug("Row validation aborted — board contains 0-values, considered invalid");
            return false;
        }

        boolean valid = IntStream.range(0, rowLength)
                .allMatch(this::isValidRow);

        if (!valid) {
            logger.debug("Row validation failed — duplicate values found");
        }

        return valid;
    }


    /**
     * Validates that every column in the board contains only unique values.
     */
    private boolean validateColumns() {
        logger.debug("Validating columns for board size: {}", rowLength);

        boolean valid = IntStream.range(0, rowLength)
                .allMatch(this::isValidColumn);

        if (!valid) {
            logger.debug("Column validation failed — duplicate values found");
        }

        return valid;
    }

    /**
     * Validates that all subgrids in the board contain only unique values.
     */
    private boolean validateSubgrids() {
        logger.debug("Validating subgrids for board size: {}, layout: {}x{}",
                rowLength, subgridSize[0], subgridSize[1]);

        int rows = subgridSize[0];
        int cols = subgridSize[1];

        boolean valid = true;

        for (int r = 0; r < rowLength; r += rows) {
            for (int c = 0; c < rowLength; c += cols) {
                if (!isValidSubgrid(r, c, rows, cols)) {
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
     * Validates that a row has no empty cells and only unique numbers.
     */
    private boolean isValidRow(int row) {
        boolean hasZero = IntStream.range(0, rowLength)
                .anyMatch(col -> sudokuBoard.getCellValue(row, col) == 0);

        if (hasZero) return false;

        return hasUniqueNumbers(
                IntStream.range(0, rowLength)
                        .map(col -> sudokuBoard.getCellValue(row, col))
        );
    }


    /**
     * Validates that a specific column contains only unique values.
     */
    private boolean isValidColumn(int col) {
        return hasUniqueNumbers(IntStream.range(0, rowLength)
                .map(row -> sudokuBoard.getCellValue(row, col)));
    }

    /**
     * Validates that a specifik subgrid contains only unique values.
     */
    private boolean isValidSubgrid(int startRow, int startCol, int subgridRows, int subgridCols) {
        return hasUniqueNumbers(
                IntStream.range(0, subgridRows)
                        .flatMap(r -> IntStream.range(0, subgridCols)
                                .map(c -> sudokuBoard.getCellValue(startRow + r, startCol + c)))
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
