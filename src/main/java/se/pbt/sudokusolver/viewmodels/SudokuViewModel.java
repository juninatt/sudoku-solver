package se.pbt.sudokusolver.viewmodels;

import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.utils.Constants;

/**
 * Acts as a link between the Sudoku board data and the UI, ensuring that board updates
 * are controlled and that the UI does not modify the {@link SudokuBoard} directly.
 * The ViewModel keeps a local copy of the board to allow safe updates and efficient UI synchronization.
 */
public class SudokuViewModel {
    private final SudokuBoard sudokuBoard;
    private final int[][] boardValues;
    private final int boardSize;
    private final int subgridSize;

    /**
     * Creates a ViewModel that prepares the Sudoku board for use in the UI.
     * It copies the board's current values and calculates necessary grid properties,
     * ensuring the UI can retrieve and modify data without directly interacting with the {@link SudokuBoard}.
     */
    public SudokuViewModel(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
        this.boardSize = sudokuBoard.getBoardSize();
        this.subgridSize = calculateSubgridSize(boardSize);
        this.boardValues = new int[boardSize][boardSize];

        initializeBoard();
    }

    /**
     * Initializes the board by copying values from {@link SudokuBoard}.
     * If the game starts with a predefined puzzle, those values will be loaded here.
     * Otherwise, all cells remain set to 0, indicating an empty board.
     */
    private void initializeBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                boardValues[row][col] = sudokuBoard.getValueAt(row, col);
            }
        }
    }

    /**
     * Attempts to set a value in a specific cell on the board.
     * If the number is within the allowed range, the ViewModel updates its local copy
     * of the board before applying the change to the {@link SudokuBoard}, which manages the game state.
     */
    public boolean setValue(int row, int col, int value) {
        if (!isWithinBounds(value)) {
            return false;
        }

        sudokuBoard.setValue(row, col, value);
        boardValues[row][col] = value;
        return true;
    }

    /**
     * Checks whether a given number falls within the valid range for the board.
     * The valid range is from 1 to the board size (inclusive).
     */
    public boolean isWithinBounds(int value) {
        return value >= 1 && value <= boardSize;
    }

    /**
     * Calculates the size of subgrids based on the total board size.
     * Standard Sudoku sizes (e.g., 9x9) have subgrids of equal size, but exceptions like 12x12 are handled separately.
     */
    private int calculateSubgridSize(int boardSize) {
        double sqrt = Math.sqrt(boardSize);
        if (sqrt % 1 == 0) {
            return (int) sqrt;
        } else if (boardSize == 12) {
            return 4;
        } else {
            throw new IllegalArgumentException(Constants.ErrorMessages.INVALID_BOARD_SIZE);
        }
    }

    /**
     * Retrieves the current value of a specific cell.
     * Used by the UI to display numbers on the board.
     */
    public int getCellValue(int row, int col) {
        return boardValues[row][col];
    }

    /**
     * Retrieves the board's size (e.g., 9 for a 9x9 Sudoku).
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Retrieves the width and height of a single subgrid.
     */
    public int getSubgridSize() {
        return subgridSize;
    }
}
