package se.pbt.sudokusolver.game.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.pbt.sudokusolver.core.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static se.pbt.sudokusolver.shared.constants.SharedConstants.EMPTY_CELL;

/**
 * Keeps a valid full completion of the current game board available on demand, without
 * relying on any single pre-computed answer key. Since puzzles are no longer generated
 * with a uniqueness guarantee, the player may fill cells in a way that diverges from the
 * board's originally generated solution while still following Sudoku rules; hint and
 * solve features must reflect that instead of contradicting the player's valid entries.
 * <p>
 * A background thread recomputes a completion after every accepted move so the cached
 * result is normally ready instantly. {@link #getSolution} additionally guarantees
 * correctness by recomputing synchronously on the rare occasion the cache hasn't caught
 * up yet with the latest move.
 */
public class LiveSolutionTracker {
    private static final Logger logger = LoggerFactory.getLogger(LiveSolutionTracker.class);

    private final SolutionGenerator solutionGenerator;
    private final ExecutorService executor;

    private volatile SudokuBoard latestSolution;

    public LiveSolutionTracker(SolutionGenerator solutionGenerator) {
        this.solutionGenerator = solutionGenerator;
        this.executor = Executors.newSingleThreadExecutor(daemonThreadFactory());
    }

    /**
     * Sets the cached completion directly, without going through the solver.
     * Used right after puzzle generation, which has already produced a solved board.
     */
    public void seed(SudokuBoard solved) {
        this.latestSolution = solved;
    }

    /**
     * Asynchronously recomputes a completion consistent with the given board snapshot,
     * replacing the cached completion once done. The caller must pass a snapshot that
     * is safe to mutate (e.g. a deep copy) and won't be touched by anyone else afterward.
     * If the board currently has no valid completion (only possible when rule-breaking
     * moves are being kept on the board), the previous cached completion is left in place.
     */
    public void requestRecompute(SudokuBoard snapshot) {
        executor.submit(() -> {
            if (solutionGenerator.fillBoardWithSolution(snapshot, 0, 0)) {
                latestSolution = snapshot;
            } else {
                logger.debug("Background solve found no valid completion for the current board state");
            }
        });
    }

    /**
     * Returns a completion that is guaranteed consistent with every filled cell on
     * {@code current}. Returns the cached completion when it already agrees with the
     * board; otherwise recomputes synchronously on the calling thread before returning.
     */
    public SudokuBoard getSolution(SudokuBoard current) {
        SudokuBoard cached = latestSolution;

        if (cached != null && isConsistent(cached, current)) {
            return cached;
        }

        logger.debug("Cached completion is stale; recomputing synchronously");
        SudokuBoard recomputed = current.deepCopy();
        if (!solutionGenerator.fillBoardWithSolution(recomputed, 0, 0)) {
            logger.warn("No valid completion exists for the current board state; returning stale cache");
            return cached;
        }

        latestSolution = recomputed;
        return recomputed;
    }

    /**
     * Returns the currently cached completion without any consistency check or fallback.
     * Package-private: exposed only so tests can observe the background recompute landing,
     * independent of {@link #getSolution}'s own synchronous-fallback behavior.
     */
    SudokuBoard peekLatestSolution() {
        return latestSolution;
    }

    /**
     * Checks whether every filled cell on {@code board} matches {@code solution} at the
     * same position.
     */
    private boolean isConsistent(SudokuBoard solution, SudokuBoard board) {
        int size = board.getRowLength();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int value = board.getCellValue(row, col);
                if (value != EMPTY_CELL && solution.getCellValue(row, col) != value) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Daemon threads so a tracker left behind by an old game (nothing currently shuts it
     * down explicitly) never prevents the JVM from exiting.
     */
    private ThreadFactory daemonThreadFactory() {
        return runnable -> {
            Thread thread = new Thread(runnable, "live-solution-tracker");
            thread.setDaemon(true);
            return thread;
        };
    }
}
