package se.pbt.sudokusolver.models;

public class SudokuBoard {
    private final int size;
    private final int[][] board;

    public SudokuBoard(int size) {
        if (size <= 0 || Math.sqrt(size) % 1 != 0) {
            throw new IllegalArgumentException("Size must be a perfect square (e.g., 4, 9, 16, 25)");
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
            throw new IllegalArgumentException("Value must be between 0 and " + size);
        }
        board[row][col] = value;
    }

    private void validatePosition(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            throw new IllegalArgumentException("Row and column must be within board size");
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
