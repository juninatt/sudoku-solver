package se.pbt.sudokusolver.core.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.core.generation.helpers.UniquenessChecker;
import se.pbt.sudokusolver.core.models.Difficulty;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static se.pbt.sudokusolver.shared.constants.Constants.GameConstants.EMPTY_CELL;
import static se.pbt.sudokusolver.shared.constants.Constants.GameConstants.ERROR_BOARD_GENERATION_FAILED;

/**
 * Builds both a solved and a playable Sudoku board based on a selected difficulty level.
 * Internally stores the solved board and generates a playable version by hiding values
 * while ensuring the puzzle retains a unique solution.
 */
public class SudokuBuilder {

    private static final Logger logger = LoggerFactory.getLogger(SudokuBuilder.class);

    int size;
    Difficulty difficulty;
    UniquenessChecker uniquenessChecker;
    SolutionGenerator solutionGenerator;

    private SudokuBoard solvedBoard;

    public SudokuBuilder(int size, Difficulty difficulty, UniquenessChecker uniquenessChecker, SolutionGenerator solutionGenerator) {
        this.size = size;
        this.difficulty = difficulty;
        this.uniquenessChecker = uniquenessChecker;
        this.solutionGenerator = solutionGenerator;
    }

    /**
     * Generates a new Sudoku puzzle with a unique solution.
     * The solved board is randomized, and a playable puzzle is derived from it.
     *
     * @return a playable {@link SudokuBoard} with a unique solution
     */
    public SudokuBoard buildSudokuPuzzle() {

        logger.info("Building new Sudoku puzzle (size: {}, difficulty: {}, target cells to hide: {})",
                size, difficulty, difficulty.calculateValuesToRemove(size));

        this.solvedBoard = createSolvedBoard();
        logger.debug("Complete sudoku board with valid solution created successfully");

        randomizeBoard(solvedBoard);
        logger.info("Re-randomized solved board to avoid pattern repetition");

        SudokuBoard playableBoard = solvedBoard.copy();

        // Remove numbers from the full sudoku puzzle solution based on difficulty
        hideCellValues(playableBoard);
        logger.info("Board generation successful");

        return playableBoard;
    }

    /**
     * Builds a fully solved Sudoku board using the {@link SolutionGenerator}.
     *
     * @return a complete, valid Sudoku solution
     */
    private SudokuBoard createSolvedBoard() {

        SudokuBoard board = new SudokuBoard(size);
        if (solutionGenerator.fillBoardWithSolution(board, 0, 0)) {
            return board.copy();
        } else {
            logger.error("Failed to create valid solution for sudoku board");
            throw new IllegalStateException(ERROR_BOARD_GENERATION_FAILED);
        }
    }

    /**
     * Removes values from a solved Sudoku board to generate a playable puzzle,
     * ensuring a unique solution remains after each removal using {@link UniquenessChecker}.
     * <p>
     * The number of hidden cells is guided by {@link Difficulty}, though uniqueness constraints
     * may prevent all targets from being met. Logs the hiding outcome and uniqueness violations.
     */
    private void hideCellValues(SudokuBoard gameBoard) {

        int hiddenCellsCount = 0;
        int uniquenessViolations = 0;

        int cellsToHideCount = difficulty.calculateValuesToRemove(size);
        List<Point> cellsToHideList = getCellsToHide(gameBoard.getSize(), cellsToHideCount);

        logger.debug("Attempting to hide up to {} cells (clue fraction: {})", cellsToHideCount, difficulty.getClueFraction());

        long startTime = System.currentTimeMillis();

        // TODO: Improve reliability of hiding logic to ensure consistent number of hidden cells across runs.
        // Revisit the timing and placement of uniqueness checks in the flow to reduce randomness and improve determinism.
        // TODO: Add UI-side logging to confirm hidden cell count and detect discrepancies between builder and view.
        for (Point p : cellsToHideList) {
            if (hiddenCellsCount >= cellsToHideCount) break;

            int cellValue = gameBoard.getValueAt(p.x, p.y);
            gameBoard.setValue(p.x, p.y, EMPTY_CELL);

            boolean unique = uniquenessChecker.hasUniqueSolution(gameBoard);

            if (!unique) {
                gameBoard.setValue(p.x, p.y, cellValue);
                uniquenessViolations++;
                logger.debug("Could not hide cell at ({}, {}) – puzzle lost uniqueness.", p.x, p.y);
            } else {
                hiddenCellsCount++;
            }
        }

        long duration = System.currentTimeMillis() - startTime;

        logger.info(
                "Cell hiding complete: {} cells successfully hidden out of {} attempts in {} ms ({} uniqueness violations)",
                hiddenCellsCount, cellsToHideList.size(), duration, uniquenessViolations
        );
    }


    /**
     * Generates a shuffled list of coordinates to attempt hiding values from.
     * Randomization helps avoid predictable puzzle structures and supports replayability.
     *
     * @return a shuffled list of cell coordinates to consider for hiding
     */
    private List<Point> getCellsToHide(int size, int cellsToHideCount) {
        List<Point> cellsList = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cellsList.add(new Point(row, col));
            }
        }
        Collections.shuffle(cellsList);

        return cellsList.subList(0, cellsToHideCount);
    }

    /**
     * Shuffles the numbers in the solved board to avoid pattern repetition,
     * while preserving the structure and validity of the solution.
     */
    private void randomizeBoard(SudokuBoard board) {
        List<Integer> mapping = IntStream.rangeClosed(1, board.getSize())
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(mapping);

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                int originalValue = board.getValueAt(row, col);

                int newValue = (originalValue != EMPTY_CELL)
                        ? mapping.get(originalValue - 1)
                        : EMPTY_CELL;

                board.setValue(row, col, newValue);
            }
        }
    }

    /**
     * Returns the internally stored solved board.
     * Typically used for hint systems or revealing full solutions.
     *
     * @return the solved {@link SudokuBoard}
     */
    public SudokuBoard getSolvedBoard() {
        return solvedBoard;
    }
}
