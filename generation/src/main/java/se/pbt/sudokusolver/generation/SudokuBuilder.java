package se.pbt.sudokusolver.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.generation.helpers.UniquenessChecker;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static se.pbt.sudokusolver.generation.constants.GenerationConstants.EMPTY_CELL;

// TODO: Make class reusable
/**
 * Builds both a solved and a playable Sudoku board based on a selected difficulty level.
 * Internally stores the solved board and generates a playable version by hiding values
 * while ensuring the puzzle retains a unique solution.
 */
public class SudokuBuilder {
    private static final Logger logger = LoggerFactory.getLogger(SudokuBuilder.class);

    private final UniquenessChecker uniquenessChecker;
    private final SolutionGenerator solutionGenerator;

    private SudokuBoard solutionBoard;

    public SudokuBuilder(UniquenessChecker uniquenessChecker,
                         SolutionGenerator solutionGenerator) {
        this.uniquenessChecker = uniquenessChecker;
        this.solutionGenerator = solutionGenerator;
    }

    /**
     * Generates a new Sudoku puzzle with a unique solution.
     * The solved board is randomized, and a playable puzzle is derived from it.
     *
     * @return a playable {@link SudokuBoard} with a unique solution
     */
    public SudokuBoard buildSudokuPuzzle(int size, double clueFraction) {
        logger.info("Building new Sudoku puzzle (size: {}, target cells to hide: {})",
                size, getCellsToHideCount(clueFraction, size));

        this.solutionBoard = createSolution(size);
        randomizeBoard(solutionBoard);
        SudokuBoard playableBoard = solutionBoard.deepCopy();

        hideCellValues(playableBoard, clueFraction);

        logger.info("Board generation successful");

        return playableBoard;
    }

    /**
     * Builds a fully solved Sudoku board using the {@link SolutionGenerator}.
     */
    private SudokuBoard createSolution(int size) {
        SudokuBoard board = new SudokuBoard(size);

        if (solutionGenerator.fillBoardWithSolution(board, 0, 0)) {
            return board.deepCopy();
        } else {
            logger.error("Failed to generate solved Sudoku board (size: {})", size);
            throw new IllegalStateException("Unable to generate a complete Sudoku solution. Invalid puzzle state.");
        }
    }

    /**
     * Removes values from a solved Sudoku board to generate a playable puzzle,
     * ensuring a unique solution remains after each removal using {@link UniquenessChecker}.
     */
    private void hideCellValues(SudokuBoard gameBoard, double clueFraction) {
        int hiddenCellsCount = 0;
        int uniquenessViolations = 0;
        int size = gameBoard.getRowLength();

        int cellsToHideCount = getCellsToHideCount(clueFraction, size);
        List<Point> cellsToHideList = getCellsToHide(size, cellsToHideCount);

        logger.debug("Starting cell hiding (boardSize: {}, clueFraction: {}, targetHiddenCells: {})",
                size, clueFraction, cellsToHideCount);

        // start and end time for performance purposes. Shown in logg
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
     * Calculates how many cells should be hidden based on the desired clue fraction
     * and the size of the board.
     */
    private static int getCellsToHideCount(double clueFraction, int size) {
        int totalCells = size * size;
        int cluesToKeep = (int) (clueFraction * totalCells);
        return totalCells - cluesToKeep;
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
        int size = board.getRowLength();
        logger.debug("Randomizing solved board values");

        List<Integer> mapping = IntStream.rangeClosed(1, size)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(mapping);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
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
     */
    public SudokuBoard getSolutionBoard() {
        return solutionBoard;
    }
}
