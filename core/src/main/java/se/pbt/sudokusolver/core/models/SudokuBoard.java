package se.pbt.sudokusolver.core.models;

import java.util.Arrays;

import static se.pbt.sudokusolver.core.constants.CoreConstants.EMPTY_CELL;

/**
 * Holds the internal state of a Sudoku board.
 * Provides a structured representation of cell values and supports logic that depends on board size.
 */
public class SudokuBoard {

    private final int[][] grid;
    private final int rowLength;
    private final int[] subgridSize;
    private int filledCells;


    /**
     * Creates an empty board of the chosen size.
     * Establishes its subgrid layout so later logic can rely on consistent dimensions.
     */
    public SudokuBoard(int rowLength) {
        this.rowLength = rowLength;
        this.grid = new int[rowLength][rowLength];
        this.subgridSize = setSubgridSize();
        this.filledCells = 0;
    }


    /**
     * Private constructor used to create a deep copy of an existing SudokuBoard.
     * Produces a fully independent instance by cloning all mutable fields.
     */
    private SudokuBoard(SudokuBoard original) {
        this.rowLength = original.rowLength;
        this.subgridSize = original.subgridSize.clone();
        this.filledCells = original.filledCells;

        this.grid = new int[rowLength][rowLength];
        for (int row = 0; row < rowLength; row++) {
            this.grid[row] = original.grid[row].clone();
        }
    }

    /**
     * Creates a deep copy of this board.
     * Delegates to the private copy-constructor to ensure full structural independence.
     */
    public SudokuBoard deepCopy() {
        return new SudokuBoard(this);
    }

    /**
     * Calculates subgrid dimensions based on the board size.
     */
    private int[] setSubgridSize() {
        return switch (rowLength) {
            case 4 -> new int[]{2, 2};
            case 6 -> new int[]{2, 3};
            case 9 -> new int[]{3, 3};
            default -> throw new IllegalArgumentException("Unsupported board size: " + rowLength);
        };
    }


    /**
     * Updates a cell and adjusts the internal count of filled positions.
     * This method is intended for use during the board construction phase only.
     * During gameplay, all value updates must go through the {@code SudokuViewModel} class,
     * which internally delegates to this method.
     */
    public void setValue(int row, int col, int value) {
        int oldValue = grid[row][col];
        grid[row][col] = value;

        boolean numberIsNotEmpty = value != EMPTY_CELL;
        boolean cellIsEmpty = oldValue == EMPTY_CELL;

        if (cellIsEmpty && numberIsNotEmpty) {
            filledCells++;
        } else if (value == EMPTY_CELL) {
            filledCells--;
        }
    }


    /**
     * Retrieves the value stored in a specific cell on the board.
     */
    public int getValueAt(int row, int col) {
        return grid[row][col];
    }

    /**
     * Get the overall width/height of the board so other logic can adapt to its size.
     */
    public int getRowLength() {
        return rowLength;
    }

    /**
     * Provides the row/column structure of each subgrid, used by validation and generation routines.
     */
    public int[] getSubgridSize() {
        return subgridSize;
    }

    /**
     * Get the number of filled cells.
     */
    public int getFilledCells() {
        return filledCells;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : grid) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}
