package se.pbt.sudokusolver.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.builders.helpers.UniquenessChecker;
import se.pbt.sudokusolver.models.SudokuBoard;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("UniquenessChecker Tests")
class UniquenessCheckerTest {

    private SudokuBoard board;
    private UniquenessChecker checker;

    @BeforeEach
    void setUp() {
        checker = new UniquenessChecker();
    }

    // -------- 4x4 --------

    @Test
    @DisplayName("Should detect exactly one solution in a valid 4x4 puzzle")
    void testUniqueSolutionForValid4x4Puzzle() {
        board = new SudokuBoard(4);
        int[][] values = {
                {1, 0, 0, 4},
                {0, 0, 1, 2},
                {0, 0, 4, 3},
                {4, 3, 2, 0}
        };
        SudokuTestBoardFactory.fillBoard(board, values);
        assertTrue(checker.hasUniqueSolution(board), "Expected exactly one solution.");
    }

    @Test
    @DisplayName("Should detect multiple solutions in empty 4x4 puzzle")
    void testMultipleSolutionsInAmbiguous4x4Puzzle() {
        board = new SudokuBoard(4);
        int[][] values = new int[4][4]; // All zeros
        SudokuTestBoardFactory.fillBoard(board, values);
        assertFalse(checker.hasUniqueSolution(board), "Expected multiple solutions.");
    }


    // -------- 6x6 --------

    @Test
    @DisplayName("Should detect unique solution in 6x6 puzzle")
    void testUniqueSolutionForValid6x6Puzzle() {
        board = new SudokuBoard(6);
        int[][] values = {
                {1, 2, 3, 4, 5, 6},
                {4, 5, 6, 1, 2, 3},
                {2, 3, 4, 5, 6, 1},
                {5, 6, 1, 2, 3, 4},
                {3, 4, 5, 6, 1, 2},
                {6, 1, 2, 3, 4, 5}
        };
        SudokuTestBoardFactory.fillBoard(board, values);
        assertTrue(checker.hasUniqueSolution(board), "Expected exactly one solution.");
    }

    @Test
    @DisplayName("Should detect multiple solutions in empty 6x6 puzzle")
    void testMultipleSolutionsIn6x6Puzzle() {
        board = new SudokuBoard(6);
        int[][] values = new int[6][6];
        SudokuTestBoardFactory.fillBoard(board, values);
        assertFalse(checker.hasUniqueSolution(board), "Expected multiple solutions.");
    }

    // -------- 9x9 --------

    @Test
    @DisplayName("Should detect unique solution in valid 9x9 puzzle")
    void testUniqueSolutionForValid9x9Puzzle() {
        board = new SudokuBoard(9);
        int[][] values = {
                {5, 3, 0, 0, 7, 0, 0, 0, 0},
                {6, 0, 0, 1, 9, 5, 0, 0, 0},
                {0, 9, 8, 0, 0, 0, 0, 6, 0},
                {8, 0, 0, 0, 6, 0, 0, 0, 3},
                {4, 0, 0, 8, 0, 3, 0, 0, 1},
                {7, 0, 0, 0, 2, 0, 0, 0, 6},
                {0, 6, 0, 0, 0, 0, 2, 8, 0},
                {0, 0, 0, 4, 1, 9, 0, 0, 5},
                {0, 0, 0, 0, 8, 0, 0, 7, 9}
        };
        SudokuTestBoardFactory.fillBoard(board, values);
        assertTrue(checker.hasUniqueSolution(board), "Expected a unique solution.");
    }

    @Test
    @DisplayName("Should detect multiple solutions in empty 9x9 puzzle")
    void testMultipleSolutionsIn9x9Puzzle() {
        board = new SudokuBoard(9);
        int[][] values = new int[9][9];
        SudokuTestBoardFactory.fillBoard(board, values);
        assertFalse(checker.hasUniqueSolution(board), "Expected multiple solutions.");
    }
}
