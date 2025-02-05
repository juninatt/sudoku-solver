package se.pbt.sudokusolver.models;

import se.pbt.sudokusolver.utils.Constants;

import java.util.Arrays;

public class SudokuBoard {
    private final int size;
    private final int[][] board;

    public SudokuBoard(int size) {
        if (size <= 0 || Math.sqrt(size) % 1 != 0) {
            throw new IllegalArgumentException(Constants.ErrorMessages.VALUE_MUST_BE + Arrays.toString(Constants.SudokuBoard.ALL_SIZES));
        }
        this.size = size;
        this.board = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public int getValue(int row, int col) {
        validatePosition(row, col);
        return board[row][col];
    }

    public void setValue(int row, int col, int value) {
        validatePosition(row, col);
        if (value < 0 || value > size) {
            throw new IllegalArgumentException(Constants.ErrorMessages.VALUE_MUST_BE + size);
        }
        board[row][col] = value;
    }

    private void validatePosition(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException(Constants.ErrorMessages.INVALID_POSITION);
        }
    }

    public boolean isSolved() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                if (board[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
