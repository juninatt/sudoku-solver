package se.pbt.sudokusolver.shared.dto;

/**
 * Data Transfer Object (DTO) for {@code SudokuBoard}.
 * Encapsulates board structure for use in UI and external modules.
 */
public record SudokuBoardDto(int[][] cells, int size, int[] subgridDimensions) {
}
