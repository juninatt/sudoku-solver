package se.pbt.sudokusolver.services;

import se.pbt.sudokusolver.models.SudokuBoard;
import se.pbt.sudokusolver.utils.Constants;

public class SudokuService {
    private SudokuBoard board;

    public void createBoard(int size) {
        board = new SudokuBoard(size);
    }

    public SudokuBoard getBoard() {
        return board;
    }

    public void setValue(int row, int col, int value) {
        if (board == null) {
            throw new IllegalStateException(Constants.ErrorMessages.BOARD_NOT_CREATED);
        } else if (!positionIsValid(row, col)) {
            throw new IllegalArgumentException(Constants.ErrorMessages.INVALID_POSITION);
        }
        board.setValue(row, col, value);

    }

    private boolean positionIsValid(int row, int col) {
        if (row < 0 || row >= board.getSize() || col < 0 || col >= board.getSize()) {
            throw new IllegalArgumentException(Constants.ErrorMessages.INVALID_POSITION);
        }
        return true;
    }

    public int getValue(int row, int col) {
        if (board == null) {
            throw new IllegalStateException(Constants.ErrorMessages.BOARD_NOT_CREATED);
        }
        return board.getValue(row, col);
    }
}
