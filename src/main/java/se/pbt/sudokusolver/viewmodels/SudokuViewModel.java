package se.pbt.sudokusolver.viewmodels;

import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.models.SudokuValidator;

/**
 * Manages the interaction between the Sudoku board data and the UI.
 * Ensures that board updates are controlled and prevents direct UI modification of the {@link SudokuBoard}.
 * The ViewModel maintains a local copy of the board state to enable safe UI updates
 * and optimize performance by reducing direct calls to the game model.
 */
public class SudokuViewModel {
    private final SudokuBoard sudokuBoard;
    private final int[][] boardValues;
    private final int boardSize;

    private final SudokuValidator validator;

    /**
     * Creates a ViewModel that serves as an intermediary between the UI and game logic.
     * It retrieves the board's initial values and calculates necessary grid properties,
     * ensuring that the UI can retrieve and modify data without directly affecting the {@link SudokuBoard}.
     */
    public SudokuViewModel(SudokuBoard sudokuBoard, SudokuValidator validator) {
        this.sudokuBoard = sudokuBoard;
        this.boardSize = sudokuBoard.getBoardSize();
        this.boardValues = new int[boardSize][boardSize];
        this.validator = validator;

        initializeBoard();
    }

    /**
     * Loads the current board state from the {@link SudokuBoard} into the local copy.
     * This ensures that the UI interacts with a cached version of the board,
     * preventing unintended modifications to the actual game state.
     */
    private void initializeBoard() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                boardValues[row][col] = sudokuBoard.getValueAt(row, col);
            }
        }
    }

    /**
     * Attempts to place a value in a specific cell on the board.
     * Ensures the move is valid before updating both the local UI copy and the actual game model.
     * If the board becomes fully filled, it triggers validation to check if the solution is correct.
     *
     * @return {@code true} if the move is valid and applied, {@code false} if it violates Sudoku rules.
     */
    public boolean setValue(int row, int col, int value) {
        if (validator.isOutOfBounds(row, col) || validator.isOutOfBounds(value)) {
            return false;
        }

        sudokuBoard.setValue(row, col, value);
        boardValues[row][col] = value;

        if (sudokuBoard.isBoardFull()) {
            return validator.validateBoard();
        }

        return true;
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
     * Returns the width and height of a single subgrid.
     * Used to correctly format the UI representation of the board.
     */
    public int getSubgridSize() {
        return sudokuBoard.getSubgridSize();
    }
}
