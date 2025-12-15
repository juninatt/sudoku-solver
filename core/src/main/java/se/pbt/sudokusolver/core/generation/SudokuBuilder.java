package se.pbt.sudokusolver.core.generation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.core.generation.helpers.UniquenessChecker;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static se.pbt.sudokusolver.core.constants.CoreConstants.EMPTY_CELL;

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

    private SudokuBoard solution;

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
    public SudokuBoard buildPlayableBoard(int gridSize, double clueFraction) {
        logger.info("Building new Sudoku puzzle (size: {}, clue fraction: {})",
                gridSize, clueFraction);

        this.solution = createSolution(gridSize);
        randomizeBoard(solution);
        SudokuBoard playableBoard = solution.deepCopy();

        hideCellValues(playableBoard, clueFraction);

        logger.info("Board generation successful");

        return playableBoard;
    }

    /**
     * Builds a fully solved Sudoku board using the {@link SolutionGenerator}.
     */
    private SudokuBoard createSolution(int gridSize) {
        SudokuBoard solution = new SudokuBoard(gridSize);

        if (solutionGenerator.fillBoardWithSolution(solution, 0, 0)) {
            return solution.deepCopy();
        } else {
            logger.error("Failed to generate solved Sudoku board (size: {})", gridSize);
            throw new IllegalStateException("Unable to generate a complete Sudoku solution. Invalid puzzle state.");
        }
    }

    /**
     * Removes values from a solved Sudoku board to generate a playable puzzle,
     * ensuring a unique solution remains after each removal using {@link UniquenessChecker}.
     */
    private void hideCellValues(SudokuBoard gameBoard, double clueFraction) {
        int gridSize = gameBoard.getRowLength();
        int totalCells = gridSize * gridSize;
        int cellsToHideCount = (int) (totalCells - (clueFraction * totalCells));

        int hiddenCellsCount = 0;
        List<Point> cellsToHideList = getCellsToHide(gridSize, cellsToHideCount);

        logger.debug("Starting cell hiding (boardSize: {}, clueFraction: {}, targetHiddenCells: {})",
                gridSize, clueFraction, cellsToHideCount);

        // start and end time for performance purposes. Shown in logg
        long startTime = System.currentTimeMillis();

        // TODO: Improve reliability of hiding logic to ensure consistent number of hidden cells across runs.
        // Revisit the timing and placement of uniqueness checks in the flow to reduce randomness and improve determinism.
        // TODO: Add UI-side logging to confirm hidden cell count and detect discrepancies between builder and view.
        for (Point p : cellsToHideList) {
            if (hiddenCellsCount >= cellsToHideCount) break;

            int oldCellValue = gameBoard.getCellValue(p.x, p.y);
            gameBoard.setValue(p.x, p.y, EMPTY_CELL);

            boolean unique = uniquenessChecker.hasUniqueSolution(gameBoard);

            if (!unique) {
                gameBoard.setValue(p.x, p.y, oldCellValue);
            } else {
                hiddenCellsCount++;
            }
        }

        long duration = System.currentTimeMillis() - startTime;

        logger.info(
                "Cell hiding complete: {} cells successfully hidden out of {} attempts in {} ms.)",
                hiddenCellsCount, cellsToHideList.size(), duration
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
        int size = board.getRowLength();
        logger.debug("Randomizing solved board values");

        List<Integer> mapping = IntStream.rangeClosed(1, size)
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(mapping);

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int originalValue = board.getCellValue(row, col);

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
        return solution;
    }
}
