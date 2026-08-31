package se.pbt.sudokusolver.shared.listeners;

/**
 * Identifies which Sudoku constraint group(s) — row and/or subgrid — a rule-breaking move
 * conflicted with, so the UI can frame exactly those groups instead of only the offending cell.
 */
public record RuleViolationScope(boolean rowViolated, boolean subgridViolated) {}
