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
    @DisplayName("Reused validator instance")
    class ReusedValidatorInstance {

        @Test
        @DisplayName("produces correct result when reused across boards of different sizes")
        void reusedAcrossDifferentBoardSizes() {
            Validator validator = new Validator();

            SudokuBoard invalid4x4 = new SudokuBoard(4);
            TestBoardFactory.applyBoardState(invalid4x4, new int[4][4]); // all zeros

            SudokuBoard valid9x9 = new SudokuBoard(9);
            TestBoardFactory.applyBoardState(valid9x9, new int[][] {
                    {5,3,4,6,7,8,9,1,2},
                    {6,7,2,1,9,5,3,4,8},
                    {1,9,8,3,4,2,5,6,7},
                    {8,5,9,7,6,1,4,2,3},
                    {4,2,6,8,5,3,7,9,1},
                    {7,1,3,9,2,4,8,5,6},
                    {9,6,1,5,3,7,2,8,4},
                    {2,8,7,4,1,9,6,3,5},
                    {3,4,5,2,8,6,1,7,9}
            });

            assertFalse(validator.validateBoard(invalid4x4),
                    "First validation (invalid 4x4) should return false");
            assertTrue(validator.validateBoard(valid9x9),
                    "Second validation (valid 9x9) must not be affected by prior board/size state");
        }
    }

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
        @DisplayName("returns false when only a single cell is empty")
        void returnsFalse_whenSingleCellIsEmpty() {
            // Otherwise-valid board with exactly one empty cell. Regression coverage for
            // validateRows(), which must catch this via its whole-board zero scan
            // rather than relying on a per-row check inside isValidRow().
            int[][] valid = {
                    {1,2,3,4},
                    {3,4,1,2},
                    {2,1,4,3},
                    {4,3,2,1}
            };
            valid[0][0] = 0;
            TestBoardFactory.applyBoardState(board, valid);
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
            // Cyclic Latin square: every row and every column is a valid permutation of 1..4,
            // but the top-left 2x2 subgrid contains duplicate values (1 and 2 each appear twice).
            int[][] bad = {
                    {1,2,3,4},
                    {2,3,4,1},
                    {3,4,1,2},
                    {4,1,2,3}
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
            // Cyclic Latin square: every row and every column is a valid permutation of 1..6,
            // but the top-left 2x3 subgrid contains duplicate values (2 and 3 each appear twice).
            int[][] bad = {
                    {1,2,3,4,5,6},
                    {2,3,4,5,6,1},
                    {3,4,5,6,1,2},
                    {4,5,6,1,2,3},
                    {5,6,1,2,3,4},
                    {6,1,2,3,4,5}
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

        @Test
        @DisplayName("returns false when only a single cell is empty")
        void returnsFalse_whenSingleCellIsEmpty_size9() {
            // Otherwise-valid board with exactly one empty cell. Regression coverage for
            // validateRows(), which must catch this via its whole-board zero scan
            // rather than relying on a per-row check inside isValidRow().
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
            valid[8][8] = 0;
            TestBoardFactory.applyBoardState(board, valid);
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
            // Cyclic Latin square: every row and every column is a valid permutation of 1..9,
            // but the top-left 3x3 subgrid contains duplicate values (2, 3 and 4 each appear
            // more than once), isolating a pure subgrid violation.
            return new int[][] {
                    {1,2,3,4,5,6,7,8,9},
                    {2,3,4,5,6,7,8,9,1},
                    {3,4,5,6,7,8,9,1,2},
                    {4,5,6,7,8,9,1,2,3},
                    {5,6,7,8,9,1,2,3,4},
                    {6,7,8,9,1,2,3,4,5},
                    {7,8,9,1,2,3,4,5,6},
                    {8,9,1,2,3,4,5,6,7},
                    {9,1,2,3,4,5,6,7,8}
            };
        }
    }
}