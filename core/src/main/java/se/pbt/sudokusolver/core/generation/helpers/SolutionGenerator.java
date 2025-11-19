package se.pbt.sudokusolver.core.generation.helpers;

import se.pbt.sudokusolver.core.models.SudokuBoard;

/**
 * Component responsible for generating a fully solved {@link SudokuBoard}.
 * Uses recursive backtracking to fill every cell with a valid value, ensuring that
 * all placements adhere to standard Sudoku rules.
 * Numbers are attempted in random order to maximize variation across generated puzzles.
 */
public class SolutionGenerator extends SudokuBuilderHelper {

    /**
     * Solves the {@link SudokuBoard} from top-left to bottom-right by filling in one cell at a time.
     * Values are placed only if they don’t break Sudoku rules. If no value fits,
     * the method reverses earlier choices and explores alternative paths.
     */
    public boolean fillBoardWithSolution(SudokuBoard board, int row, int col) {
        int boardSize = board.getSize();

        if (isBoardFull(row, boardSize)) return true;

        if (board.hasValueAt(row, col)) {
            int[] next = getNextCell(row, col, boardSize);
            return fillBoardWithSolution(board, next[0], next[1]);
        }

        return tryNumbersInCell(board, row, col, num -> {
            int[] next = getNextCell(row, col, boardSize);
            return fillBoardWithSolution(board, next[0], next[1]);
        });
    }
}
