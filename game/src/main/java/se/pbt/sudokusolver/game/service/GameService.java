package se.pbt.sudokusolver.game.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.core.generation.SudokuBuilder;
import se.pbt.sudokusolver.core.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.shared.game.PuzzleDifficulty;
import se.pbt.sudokusolver.shared.listeners.CellViewListener;
import se.pbt.sudokusolver.shared.listeners.RuleViolationListener;
import se.pbt.sudokusolver.shared.listeners.RuleViolationScope;
import se.pbt.sudokusolver.validation.Validator;

import static se.pbt.sudokusolver.shared.constants.SharedConstants.EMPTY_CELL;
import static se.pbt.sudokusolver.game.constants.GameConstants.MIN_CELL_VALUE;

/**
 * Coordinates creation and management of playable Sudoku games.
 * Delegates puzzle generation to the generation module and exposes game state to UI components.
 */
public class GameService {
    private static final Logger logger = LoggerFactory.getLogger(GameService.class);

    private final SudokuBuilder sudokuBuilder;
    private final Validator validator;
    private final LiveSolutionTracker liveSolutionTracker;

    private SudokuBoard gameBoard;

    private boolean cheatModeEnabled;
    private boolean endGameOnMistake;
    private boolean gameOver;

    private CellViewListener cellViewListener = (r, c, v) -> {};
    private RuleViolationListener ruleViolationListener = (r, c, gameOver, scope) -> {};

    /**
     * Constructs a GameService with required dependencies for validation and puzzle generation.
     */
    public GameService(Validator validator) {
        SolutionGenerator solutionGenerator = new SolutionGenerator();

        this.sudokuBuilder = new SudokuBuilder(solutionGenerator);
        this.validator = validator;
        this.liveSolutionTracker = new LiveSolutionTracker(solutionGenerator);
    }

    /**
     * Builds a new playable Sudoku game using the configured difficulty level.
     */
    public void buildPlayableGame(int size, PuzzleDifficulty difficulty) {
        logger.info("Building new playable game (size: {}, difficulty: {})", size, difficulty);
        gameBoard = sudokuBuilder.buildPlayableBoard(size, difficulty.getClueFraction());
        liveSolutionTracker.seed(sudokuBuilder.getSolutionBoard());
        gameOver = false;
        logger.debug("New game constructed successfully");
    }

    /**
     * Configures how the game reacts to moves that break Sudoku rules during play.
     * Must be called before the player starts making moves; defaults to both disabled,
     * which preserves the original behavior of accepting any structurally legal move.
     *
     * @param cheatModeEnabled if a move breaks the rules, revert it so the player can retry
     * @param endGameOnMistake if a move breaks the rules, end the game immediately.
     *                         Takes priority over {@code cheatModeEnabled} when both are enabled.
     */
    public void configureRules(boolean cheatModeEnabled, boolean endGameOnMistake) {
        this.cheatModeEnabled = cheatModeEnabled;
        this.endGameOnMistake = endGameOnMistake;
    }

    /**
     * Attempts to set a value in the playable board.
     * Returns false if the move is illegal, violates board boundaries, or the game has ended.
     * A move that is placed but breaks Sudoku rules is still reported as {@code true} here;
     * how it's then handled (kept, reverted, or ending the game) depends on {@link #configureRules}
     * and is reported separately via {@link #setRuleViolationListener}.
     */
    public boolean setValue(int row, int col, int value) {
        if (gameOver) {
            logger.debug("Rejected setValue request: game is already over");
            return false;
        }
        if (outOfBounds(row, col, value)) {
            logger.warn("Rejected setValue request: out of bounds (row={}, col={}, value={})", row, col, value);
            return false;
        }
        if (gameBoard.getCellValue(row, col) != EMPTY_CELL) {
            logger.debug("Rejected setValue: cell ({},{}) is not empty", row, col);
            return false;
        }

        placeValue(row, col, value);

        if (!validator.validateCell(gameBoard, row, col)) {
            handleRuleViolation(row, col);
        }

        return true;
    }

    /**
     * Reacts to a move that broke Sudoku rules, according to the configured rule-enforcement mode.
     * If neither mode is enabled, the move is silently kept — matching the original, permissive behavior.
     */
    private void handleRuleViolation(int row, int col) {
        if (endGameOnMistake) {
            gameOver = true;
            logger.info("Move at ({},{}) broke Sudoku rules — ending game", row, col);
            ruleViolationListener.onRuleViolation(row, col, true, scopeOf(row, col));
        } else if (cheatModeEnabled) {
            logger.debug("Move at ({},{}) broke Sudoku rules — reverting for retry", row, col);
            RuleViolationScope scope = scopeOf(row, col);
            gameBoard.setValue(row, col, EMPTY_CELL);
            cellViewListener.onCellUpdated(row, col, EMPTY_CELL);
            ruleViolationListener.onRuleViolation(row, col, false, scope);
        }
    }

    /**
     * Reports which constraint group(s) the value currently at {@code (row, col)} conflicts with.
     * Must be called while the offending value is still on the board, before any revert.
     */
    private RuleViolationScope scopeOf(int row, int col) {
        Validator.CellViolation violation = validator.checkCell(gameBoard, row, col);
        return new RuleViolationScope(!violation.rowValid(), !violation.subgridValid());
    }

    /**
     * Checks whether a requested move is outside valid board or value ranges.
     */
    private boolean outOfBounds(int row, int col, int value) {
        int limit = gameBoard.getRowLength();
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
        int toReveal = gameBoard.getRowLength() * gameBoard.getRowLength();
        revealCells(toReveal);
    }

    /**
     * Reveals up to maxCells missing values from a completion consistent with the current
     * board. Places values directly via {@link #placeValue}, bypassing rule enforcement:
     * a revealed value always matches that completion and must never be treated as a
     * mistake, even if it conflicts with a player's own (rule-valid but diverging) entries.
     */
    private void revealCells(int maxCells) {
        SudokuBoard solution = liveSolutionTracker.getSolution(gameBoard);
        int revealed = 0;
        int size = solution.getRowLength();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (gameBoard.getCellValue(r, c) == EMPTY_CELL) {
                    placeValue(r, c, solution.getCellValue(r, c));
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
     * Writes a value directly to the board and notifies listeners, without any rule enforcement.
     * Shared by {@link #setValue} (after its own legality checks) and {@link #revealCells}.
     * Every accepted placement also kicks off a background recompute of the live completion,
     * so hint/solve stay consistent with whatever the player (or a reveal) just wrote.
     */
    private void placeValue(int row, int col, int value) {
        gameBoard.setValue(row, col, value);
        cellViewListener.onCellUpdated(row, col, value);
        liveSolutionTracker.requestRecompute(gameBoard.deepCopy());

        int rowLength = gameBoard.getRowLength();
        int gridSize = rowLength * rowLength;
        if (gameBoard.getFilledCells() == gridSize) {
            logger.info("Board is full, triggering validation");
            validator.validateBoard(gameBoard);
        }
    }

    /**
     * Returns subgrid dimensions used by the current board.
     */
    public int[] getSubgridDimensions() {
        return gameBoard.getSubgridSize();
    }

    /**
     * Retrieves the value in a specific board cell.
     */
    public int getCellValue(int row, int col) {
        return gameBoard.getCellValue(row, col);
    }

    /**
     * Returns whether the game has ended due to a rule-breaking move under end-on-mistake mode.
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Assigns the listener responsible for reacting to cell updates.
     * If {@code null} is provided, a no-op listener is applied to ensure safe calls without null checks.
     */
    public void setCellViewListener(CellViewListener listener) {
        this.cellViewListener = listener != null ? listener : (r, c, v) -> {};
    }

    /**
     * Assigns the listener notified when a move breaks Sudoku rules during play.
     * If {@code null} is provided, a no-op listener is applied to ensure safe calls without null checks.
     */
    public void setRuleViolationListener(RuleViolationListener listener) {
        this.ruleViolationListener = listener != null ? listener : (r, c, gameOver, scope) -> {};
    }

    /**
     * Returns the current playable Sudoku board for the active game session.
     */
    public SudokuBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Returns a valid full completion consistent with the current game board.
     * Not a fixed answer key: as the player fills cells, this reflects the live board
     * rather than the puzzle's originally generated (and possibly since-diverged-from) solution.
     */
    public SudokuBoard getSolutionBoard() {
        return liveSolutionTracker.getSolution(gameBoard);
    }

    /**
     * Returns the {@link Validator} used by this service.
     * Package-private: exposed only so tests can verify the injected dependency
     * without requiring the {@code validator} field itself to be package-visible.
     */
    Validator getValidator() {
        return validator;
    }
}