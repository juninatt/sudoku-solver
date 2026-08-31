package se.pbt.sudokusolver.game.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.core.generation.helpers.SolutionGenerator;
import se.pbt.sudokusolver.core.models.SudokuBoard;

import static org.junit.jupiter.api.Assertions.*;
import static se.pbt.sudokusolver.shared.constants.SharedConstants.EMPTY_CELL;

@DisplayName("LiveSolutionTracker:")
class LiveSolutionTrackerTest {

    private static final int SIZE = 9;

    private SudokuBoard solve(SolutionGenerator generator) {
        SudokuBoard board = new SudokuBoard(SIZE);
        assertTrue(generator.fillBoardWithSolution(board, 0, 0));
        return board;
    }

    @Test
    @DisplayName("getSolution returns the seeded board when it is consistent with the current board")
    void getSolution_returnsSeededBoard_whenConsistent() {
        SolutionGenerator generator = new SolutionGenerator();
        LiveSolutionTracker tracker = new LiveSolutionTracker(generator);

        SudokuBoard seeded = solve(generator);
        tracker.seed(seeded);

        SudokuBoard current = seeded.deepCopy();
        // Empty a few cells; remaining filled cells still agree with the seeded completion.
        current.setValue(0, 0, EMPTY_CELL);
        current.setValue(1, 1, EMPTY_CELL);

        SudokuBoard result = tracker.getSolution(current);

        assertSame(seeded, result, "Cached completion should be returned as-is when still consistent");
    }

    @Test
    @DisplayName("getSolution recomputes synchronously and honors the current board when the cache has diverged")
    void getSolution_recomputes_whenCacheDiverged() {
        SolutionGenerator generator = new SolutionGenerator();
        LiveSolutionTracker tracker = new LiveSolutionTracker(generator);

        SudokuBoard seeded = solve(generator);
        tracker.seed(seeded);

        // Build a current board with a single given that conflicts with the seeded completion.
        SudokuBoard current = new SudokuBoard(SIZE);
        int conflictingValue = (seeded.getCellValue(0, 0) % SIZE) + 1;
        current.setValue(0, 0, conflictingValue);

        SudokuBoard result = tracker.getSolution(current);

        assertNotSame(seeded, result, "A diverged board must trigger a fresh completion");
        assertEquals(conflictingValue, result.getCellValue(0, 0),
                "Recomputed completion must honor the current board's given values");
    }

    @Test
    @DisplayName("requestRecompute asynchronously updates the cached completion")
    void requestRecompute_updatesCacheAsynchronously() throws InterruptedException {
        SolutionGenerator generator = new SolutionGenerator();
        LiveSolutionTracker tracker = new LiveSolutionTracker(generator);

        SudokuBoard seeded = solve(generator);
        tracker.seed(seeded);

        SudokuBoard snapshot = new SudokuBoard(SIZE);
        int givenValue = (seeded.getCellValue(0, 0) % SIZE) + 1;
        snapshot.setValue(0, 0, givenValue);

        tracker.requestRecompute(snapshot);

        SudokuBoard updated = null;
        for (int attempt = 0; attempt < 100; attempt++) {
            SudokuBoard candidate = tracker.peekLatestSolution();
            if (candidate != null && candidate.getCellValue(0, 0) == givenValue) {
                updated = candidate;
                break;
            }
            Thread.sleep(20);
        }

        assertNotNull(updated, "Background recompute should eventually update the cached completion");
        assertEquals(givenValue, updated.getCellValue(0, 0));
    }
}
