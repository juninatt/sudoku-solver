package se.pbt.sudokusolver.game.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.generation.SudokuBuilder;
import se.pbt.sudokusolver.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.generation.helpers.UniquenessChecker;
import se.pbt.sudokusolver.shared.game.PuzzleDifficulty;
import se.pbt.sudokusolver.shared.listeners.CellViewListener;
import se.pbt.sudokusolver.validation.Validator;

import static se.pbt.sudokusolver.game.constants.GameConstants.EMPTY_CELL;
import static se.pbt.sudokusolver.game.constants.GameConstants.MIN_CELL_VALUE;

/**
 * Coordinates creation and management of playable Sudoku games.
 * Delegates puzzle generation to the generation module and exposes game state to UI components.
 */
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    SudokuBuilder sudokuBuilder;
    Validator validator;

    SudokuBoard gameBoard;
    SudokuBoard solutionBoard;

    private CellViewListener cellViewListener = (r, c, v) -> {};

    /**
     * Constructs a GameService with required dependencies for validation and puzzle generation.
     */
    public GameService(Validator validator) {
        UniquenessChecker uniquenessChecker = new UniquenessChecker();
        SolutionGenerator solutionGenerator = new SolutionGenerator();

        this.sudokuBuilder = new SudokuBuilder(uniquenessChecker, solutionGenerator);
        this.validator = validator;
    }

    /**
     * Builds a new playable Sudoku game using the configured difficulty level.
     */
    public void buildPlayableGame(int size, PuzzleDifficulty difficulty) {
        logger.info("Building new playable game (size: {}, difficulty: {})", size, difficulty);
        gameBoard = sudokuBuilder.buildSudokuPuzzle(size, difficulty.getClueFraction());
        solutionBoard = sudokuBuilder.getSolutionBoard();
        logger.debug("New game constructed successfully");
    }

    /**
     * Attempts to set a value in the playable board.
     * Returns false if the move is illegal or violates board boundaries.
     */
    public boolean setValue(int row, int col, int value) {
        if (outOfBounds(row, col, value)) {
            logger.warn("Rejected setValue request: out of bounds (row={}, col={}, value={})", row, col, value);
            return false;
        }
        if (gameBoard.getValueAt(row, col) != EMPTY_CELL) {
            logger.debug("Rejected setValue: cell ({},{}) is not empty", row, col);
            return false;
        }

        gameBoard.setValue(row, col, value);
        cellViewListener.onCellUpdated(row, col, value);

        if (gameBoard.isBoardFull()) {
            logger.info("Board is full, triggering validation");
            validator.validateBoard(gameBoard);
        }

        return true;
    }

    /**
     * Checks whether a requested move is outside valid board or value ranges.
     */
    private boolean outOfBounds(int row, int col, int value) {
        int limit = gameBoard.getSize();
        return (row < 0 || row >= limit ||
                col < 0 || col >= limit ||
                value < MIN_CELL_VALUE || value > limit);
    }

    /**
     * Reveals a single correct value from the solved board.
     * Used for hint functionality.
     */
    public void revealSolutionCellValue() {
        logger.info("Revealing a single solution cell");
        revealCells(1);
    }

    /**
     * Reveals all solution values, fully solving the board for the player.
     */
    public void revealFullSolution() {
        logger.info("Revealing full solution");
        int toReveal = solutionBoard.getSize() * solutionBoard.getSize();
        revealCells(toReveal);
    }

    /**
     * Reveals up to maxCells missing values from the solution board.
     */
    private void revealCells(int maxCells) {
        int revealed = 0;
        int size = solutionBoard.getSize();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (gameBoard.getValueAt(r, c) == EMPTY_CELL) {
                    setValue(r, c, solutionBoard.getValueAt(r, c));
                    if (++revealed >= maxCells) {
                        logger.debug("Revealed {} cell(s)", revealed);
                        return;
                    }
                }
            }
        }
        logger.debug("Reveal completed with {} cells updated", revealed);
    }

    /**
     * Returns subgrid dimensions used by the current board.
     */
    public int[] getSubgridDimensions() {
        return gameBoard.getSubgridDimensions();
    }

    /**
     * Retrieves the value in a specific board cell.
     */
    public int getCellValue(int row, int col) {
        return gameBoard.getValueAt(row, col);
    }

    /**
     * Assigns the listener responsible for reacting to cell updates.
     * If {@code null} is provided, a no-op listener is applied to ensure safe calls without null checks.
     */
    public void setCellViewListener(CellViewListener listener) {
        this.cellViewListener = listener != null ? listener : (r, c, v) -> {};
    }

    /**
     * Returns the current playable Sudoku board for the active game session.
     */
    public SudokuBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Returns the solved version of the current Sudoku puzzle.
     */
    public SudokuBoard getSolutionBoard() {
        return solutionBoard;
    }
}
