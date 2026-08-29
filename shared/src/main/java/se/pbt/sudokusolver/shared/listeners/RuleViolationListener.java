package se.pbt.sudokusolver.shared.listeners;

/**
 * UI listener notified when a player's move breaks Sudoku rules during gameplay.
 * Only invoked when rule enforcement is active (cheat mode or end-on-mistake); a move
 * that violates the rules while neither option is enabled is accepted silently.
 */
@FunctionalInterface
public interface RuleViolationListener {

    /**
     * Notifies that the move at {@code (row, col)} violated Sudoku rules.
     *
     * @param row      the row of the offending cell
     * @param col      the column of the offending cell
     * @param gameOver {@code true} if the violation ended the game (end-on-mistake mode),
     *                 {@code false} if the move was instead reverted so the player can retry (cheat mode)
     */
    void onRuleViolation(int row, int col, boolean gameOver);
}