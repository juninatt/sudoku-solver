package se.pbt.sudokusolver.shared.dto;

/**
 * Container for data needed to start a new Sudoku game.
 * Includes puzzle board, solved board and difficulty level.
 */
public record NewGameData(SudokuBoardDto board, SudokuBoardDto solution) {
}
