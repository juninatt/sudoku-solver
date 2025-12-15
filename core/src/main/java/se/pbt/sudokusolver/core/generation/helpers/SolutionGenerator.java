package se.pbt.sudokusolver.core.generation.helpers;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import static se.pbt.sudokusolver.core.constants.CoreConstants.EMPTY_CELL;

/**
 * Generates a complete solved board.
 * Backtracking is used to ensure each placement allows the remaining board to still be solvable.
 * Number order is randomized to prevent repeated patterns and make the puzzles structurally different between games.
 */
public final class  SolutionGenerator extends SudokuBuilderHelper {
    private static final Logger logger = LoggerFactory.getLogger(SolutionGenerator.class);

    /**
     * Attempts to fill the board with a valid full solution.
     * Recursion is used because invalid placements require reverting earlier decisions
     * to test alternate values, which cannot be solved correctly with a single forward pass.
     *
     */
    public boolean fillBoardWithSolution(SudokuBoard board, int row, int col) {
        int rowLength = board.getRowLength();
        logger.debug("Starting solution generation for size={}", rowLength);

        if (row >= rowLength) {
            logger.info("Reached end of board (row={}, col={}), solution path valid", row, col);
            return true;
        }

        if (board.getCellValue(row, col) != EMPTY_CELL) {
            int[] nextCellPos = getNextCellPos(row, col, rowLength);
            return fillBoardWithSolution(board, nextCellPos[0], nextCellPos[1]);
        }

        boolean solved = tryNumbersInCell(board, row, col, num -> {
            int[] next = getNextCellPos(row, col, rowLength);
            return fillBoardWithSolution(board, next[0], next[1]);
        });

        if (!solved) {
            logger.debug("Backtracking at (row={}, col={})", row, col);
        }

        return solved;
    }
}
