package se.pbt.sudokusolver.services;

import se.pbt.sudokusolver.models.SudokuBoard;

public class SudokuService {
    private SudokuBoard board;

    public void createBoard(int size) {
        board = new SudokuBoard(size);
    }

    public SudokuBoard getBoard() {
        return board;
    }

    public boolean isSolved() {
        return board != null && board.isSolved();
    }

    public void setValue(int row, int col, int value) {
        if (board == null) {
            throw new IllegalStateException("No board has been created yet!");
        }
        board.setValue(row, col, value);
    }

    public int getValue(int row, int col) {
        if (board == null) {
            throw new IllegalStateException("No board has been created yet!");
        }
        return board.getValue(row, col);
    }
}
