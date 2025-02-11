package se.pbt.sudokusolver.models;

import java.util.Arrays;

public class SudokuBoard {
    private final int size;
    private int filledCells;
    private final int[][] board;

    public SudokuBoard(int size) {
        this.size = size;
        filledCells = 0;
        this.board = new int[size][size];
    }

    public int getSize() {
        return size;
    }

    public int getValue(int row, int col) {
        return board[row][col];
    }

    public void setValue(int row, int col, int value) {
        board[row][col] = value;
        filledCells++;
        if (filledCells == size * size)
            SudokuValidator.validateBoard(this);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        return sb.toString();
    }
}
