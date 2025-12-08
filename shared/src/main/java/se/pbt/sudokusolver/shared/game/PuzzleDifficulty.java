package se.pbt.sudokusolver.shared.game;

/**
 * Transport-neutral contract describing puzzle difficulty for board generation.
 * UI-agnostic, contains no JavaFX or backend logic dependencies.
 */
@FunctionalInterface
public interface PuzzleDifficulty {
    double getClueFraction();
}
