package se.pbt.sudokusolver.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.validation.testutil.TestBoardFactory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Sudoku Validator")
public class ValidatorTest {

    @Nested
    @DisplayName("4x4 board")
    class Validator4x4 {

        private Validator validator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(4);
            validator = new Validator();
        }

        @Test
        @DisplayName("returns false when row contains 0")
        void returnsFalse_whenContainsZero() {
            int[][] zeros = new int[4][4];
            TestBoardFactory.applyBoardState(board, zeros);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns true when column has all unique numbers")
        void returnsTrue_whenColumnContains_allUniqueNumbers() {
            int[][] valid = {
                    {1,2,3,4},
                    {3,4,1,2},
                    {2,1,4,3},
                    {4,3,2,1}
            };
            TestBoardFactory.applyBoardState(board, valid);
            assertTrue(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when row has duplicates")
        void returnsFalse_whenRowHasDuplicates() {
            int[][] bad = {
                    {1,2,3,4},
                    {1,2,3,4},
                    {2,1,4,3},
                    {4,3,2,1}
            };
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when column has duplicates")
        void returnsFalse_whenColumnHasDuplicates() {
            int[][] bad = {
                    {1,2,3,4},
                    {3,4,1,2},
                    {1,2,4,3},
                    {4,3,2,1}
            };
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when subgrid has duplicates")
        void returnsFalse_whenSubgridHasDuplicates() {
            int[][] bad = {
                    {1,2,3,4},
                    {3,4,1,2},
                    {1,2,4,3},
                    {4,3,2,1}
            };
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }
    }

    @Nested
    @DisplayName("6x6 board")
    class Validator6x6 {

        private Validator validator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(6);
            validator = new Validator();
        }

        @Test
        @DisplayName("returns false when board contains only 0")
        void returnsFalse_whenContainsZero_size6() {
            int[][] zeros = new int[6][6];
            TestBoardFactory.applyBoardState(board, zeros);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns true when all columns contain unique numbers")
        void returnsTrue_whenColumnContains_allUniqueNumbers_size6() {
            int[][] valid = {
                    {1,2,3,4,5,6},
                    {4,5,6,1,2,3},
                    {2,3,1,5,6,4},
                    {5,6,4,2,3,1},
                    {3,1,2,6,4,5},
                    {6,4,5,3,1,2}
            };
            TestBoardFactory.applyBoardState(board, valid);
            assertTrue(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when a row has duplicates")
        void returnsFalse_whenRowHasDuplicates_size6() {
            int[][] bad = {
                    {1,2,3,4,5,6},
                    {1,2,3,4,5,6},
                    {2,3,1,5,6,4},
                    {5,6,4,2,3,1},
                    {3,1,2,6,4,5},
                    {6,4,5,3,1,2}
            };
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when a column has duplicates")
        void returnsFalse_whenColumnHasDuplicates_size6() {
            int[][] bad = {
                    {1,2,3,4,5,6},
                    {4,5,6,1,2,3},
                    {1,3,1,5,6,4},
                    {5,6,4,2,3,1},
                    {3,1,2,6,4,5},
                    {6,4,5,3,1,2}
            };
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when a subgrid has duplicates")
        void returnsFalse_whenSubgridHasDuplicates_size6() {
            int[][] bad = {
                    {1,2,3,4,5,6},
                    {4,5,6,1,2,3},
                    {1,2,3,4,5,6},
                    {5,6,4,2,3,1},
                    {3,1,2,6,4,5},
                    {6,4,5,3,1,2}
            };
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }
    }

    @Nested
    @DisplayName("9x9 board")
    class Validator9x9 {

        private Validator validator;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            board = new SudokuBoard(9);
            validator = new Validator();
        }

        @Test
        @DisplayName("returns false when board contains only 0")
        void returnsFalse_whenContainsZero_size9() {
            int[][] zeros = new int[9][9];
            TestBoardFactory.applyBoardState(board, zeros);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns true when all columns have unique numbers")
        void returnsTrue_whenColumnContains_allUniqueNumbers_size9() {
            int[][] valid = {
                    {5,3,4,6,7,8,9,1,2},
                    {6,7,2,1,9,5,3,4,8},
                    {1,9,8,3,4,2,5,6,7},
                    {8,5,9,7,6,1,4,2,3},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {9,6,1,5,3,7,2,8,4},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };
            TestBoardFactory.applyBoardState(board, valid);
            assertTrue(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when a row has duplicates")
        void returnsFalse_whenRowHasDuplicates_size9() {
            int[][] bad = validDuplicateRow();
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when a column has duplicates")
        void returnsFalse_whenColumnHasDuplicates_size9() {
            int[][] bad = validDuplicateColumn();
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }

        @Test
        @DisplayName("returns false when a subgrid has duplicates")
        void returnsFalse_whenSubgridHasDuplicates_size9() {
            int[][] bad = validDuplicateSubgrid();
            TestBoardFactory.applyBoardState(board, bad);
            assertFalse(validator.validateBoard(board));
        }

        private int[][] validDuplicateRow() {
            return new int[][] {
                    {5,3,4,6,7,8,9,1,2},
                    {5,3,4,6,7,8,9,1,2},
                    {1,9,8,3,4,2,5,6,7},
                    {8,5,9,7,6,1,4,2,3},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {9,6,1,5,3,7,2,8,4},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };
        }

        private int[][] validDuplicateColumn() {
            return new int[][] {
                    {5,3,4,6,7,8,9,1,2},
                    {6,7,2,1,9,5,3,4,8},
                    {1,9,8,3,4,2,5,6,7},
                    {8,5,9,7,6,1,4,2,3},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {5,3,4,6,7,8,9,1,2},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };
        }

        private int[][] validDuplicateSubgrid() {
            return new int[][] {
                    {5,3,4,6,7,8,9,1,2},
                    {6,7,2,1,9,5,3,4,8},
                    {1,9,8,3,4,2,5,6,7},
                    {5,3,4,1,2,3,6,7,8},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {9,6,1,5,3,7,2,8,4},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            };
        }
    }
}
