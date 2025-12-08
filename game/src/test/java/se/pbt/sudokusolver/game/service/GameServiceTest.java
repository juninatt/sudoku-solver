package se.pbt.sudokusolver.game.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.core.models.SudokuBoard;
import se.pbt.sudokusolver.game.model.Difficulty;
import se.pbt.sudokusolver.validation.Validator;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static se.pbt.sudokusolver.game.constants.GameConstants.EMPTY_CELL;

@DisplayName("GameService:")
public class GameServiceTest {

    @Nested
    @DisplayName("Construction")
    class ConstructionTests {

        @Test
        @DisplayName("creates GameService with required dependencies")
        void createsGameService_withDependencies() {
            Validator validator = new Validator();
            GameService service = new GameService(validator);

            assertNotNull(service);
        }

        @Test
        @DisplayName("stores provided Validator instance")
        void storesInjectedValidator() {
            Validator validator = new Validator();
            GameService service = new GameService(validator);

            assertSame(validator, service.validator);
        }
    }



    @Nested
    @DisplayName("Build playable game")
    class BuildPlayableGameTests {

        @Test
        @DisplayName("creates playable and solution boards of correct size")
        void createsBoards_withCorrectSize() {
            GameService service = new GameService(new Validator());
            service.buildPlayableGame(9, Difficulty.EASY);

            SudokuBoard gameBoard = service.getGameBoard();
            SudokuBoard solutionBoard = service.getSolutionBoard();

            assertEquals(9, gameBoard.getSize());
            assertEquals(9, solutionBoard.getSize());
        }

        @Test
        @DisplayName("solution board is fully filled")
        void solutionBoard_isFullyFilled() {
            GameService service = new GameService(new Validator());
            service.buildPlayableGame(9, Difficulty.EASY);

            SudokuBoard solution = service.getSolutionBoard();

            assertEquals(
                    0,
                    countEmptyCells(solution),
                    "Solution board must not contain empty cells"
            );
        }


        @Test
        @DisplayName("playable board contains empty cells according to difficulty")
        void playableBoard_respectsDifficultyClueFraction() {
            GameService service = new GameService(new Validator());
            service.buildPlayableGame(9, Difficulty.EASY);

            SudokuBoard playable = service.getGameBoard();
            int size = playable.getSize();
            int total = size * size;

            int emptyCount = countEmptyCells(playable);
            int filledCount = countFilledCells(playable);

            assertTrue(emptyCount > 0, "Playable board must contain at least one empty cell");
            assertTrue(filledCount > 0, "Playable board must contain at least one filled cell");
            assertEquals(total, emptyCount + filledCount,
                    "Sum of empty and filled cells must equal total cell count");
        }


        @Test
        @DisplayName("playable and solution boards are not the same instance")
        void boardsAreNotSameReference() {
            GameService service = new GameService(new Validator());
            service.buildPlayableGame(9, Difficulty.EASY);

            SudokuBoard gameBoard = service.getGameBoard();
            SudokuBoard solutionBoard = service.getSolutionBoard();

            assertNotSame(gameBoard, solutionBoard,
                    "Playable and solution boards must not be the same instance");
        }
    }



    @Nested
    @DisplayName("setValue()")
    class SetValueTests {

        private GameService service;
        private SudokuBoard board;
        private Validator validator;
        private boolean listenerTriggered;

        @BeforeEach
        void setUp() {
            validator = mock(Validator.class);
            service = new GameService(validator);
            service.buildPlayableGame(9, Difficulty.EASY);

            board = service.getGameBoard();
            listenerTriggered = false;

            service.setCellViewListener((r, c, v) -> listenerTriggered = true);
        }


        @Test
        @DisplayName("returns false when move is out of bounds")
        void returnsFalse_whenOutOfBounds() {
            assertFalse(service.setValue(-1, 0, 5));
            assertFalse(service.setValue(0, -1, 5));
            assertFalse(service.setValue(9, 0, 5));
            assertFalse(service.setValue(0, 9, 5));
            assertFalse(service.setValue(0, 0, 0));   // too low
            assertFalse(service.setValue(0, 0, 10));  // too high
        }

        @Test
        @DisplayName("returns false when cell is not empty")
        void returnsFalse_whenCellNotEmpty() {
            Point filled = findFilledCell(board);
            assertFalse(service.setValue(filled.x, filled.y, 9));
        }

        @Test
        @DisplayName("updates cell when move is valid")
        void updatesCell_whenValid() {
            clearBoard(board);
            Point empty = findEmptyCell(board);

            boolean ok = service.setValue(empty.x, empty.y, 7);

            assertTrue(ok);
            assertEquals(7, board.getValueAt(empty.x, empty.y));
        }

        @Test
        @DisplayName("triggers validation when board becomes full")
        void triggersValidation_whenBoardIsFull() {
            clearBoard(board);

            fillAllExceptLast(1);
            Point last = new Point(board.getSize() - 1, board.getSize() - 1);

            service.setValue(last.x, last.y, 1);

            verify(validator, times(1)).validateBoard(any());
        }

        @Test
        @DisplayName("does not trigger validation before board is full")
        void doesNotTriggerValidation_beforeBoardFull() {
            clearBoard(board);
            Point empty = findEmptyCell(board);

            service.setValue(empty.x, empty.y, 5);

            verify(validator, never()).validateBoard(any());
        }

        @Test
        @DisplayName("triggers cell listener on valid update")
        void triggersCellViewListener_onSuccessfulUpdate() {
            clearBoard(board);
            Point empty = findEmptyCell(board);

            service.setValue(empty.x, empty.y, 8);

            assertTrue(listenerTriggered);
        }

        @Test
        @DisplayName("does not trigger cell listener when update is rejected")
        void doesNotTriggerListener_onRejectedUpdate() {
            Point filled = findFilledCell(board);

            service.setValue(filled.x, filled.y, 9);

            assertFalse(listenerTriggered);
        }

        // Helpers

        private void fillAllExceptLast(int fillValue) {
            int size = board.getSize();
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (r == size - 1 && c == size - 1) continue;
                    board.setValue(r, c, fillValue);
                }
            }
        }
    }



    @Nested
    @DisplayName("Reveal solution")
    class RevealSolutionTests {

        private GameService service;
        private SudokuBoard gameBoard;
        private SudokuBoard solutionBoard;

        @BeforeEach
        void setUp() {
            service = new GameService(new Validator());
            service.buildPlayableGame(9, Difficulty.EASY);

            // Prevent NullPointerException when GameService calls cellViewListener.onCellUpdated(...)
            service.setCellViewListener((r, c, v) -> {});

            gameBoard = service.getGameBoard();
            solutionBoard = service.getSolutionBoard();
        }


        @Test
        @DisplayName("reveals exactly one missing cell")
        void revealOneCell_revealsOne() {
            clearBoard(gameBoard);

            int before = countEmptyCells(gameBoard);
            service.revealSolutionCellValue();
            int after = countEmptyCells(gameBoard);

            assertEquals(before - 1, after, "Exactly one new cell should be revealed");
        }

        @Test
        @DisplayName("revealing one cell does nothing when board is full")
        void revealOneCell_noOpWhenBoardFull() {
            int size = gameBoard.getSize();
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    gameBoard.setValue(r, c, solutionBoard.getValueAt(r, c));
                }
            }

            int before = countEmptyCells(gameBoard);
            service.revealSolutionCellValue();
            int after = countEmptyCells(gameBoard);

            assertEquals(before, after, "Reveal should have no effect when the board is already full");
        }

        @Test
        @DisplayName("reveals full solution")
        void revealFullSolution_fillsAllCells() {
            clearBoard(gameBoard);

            service.revealFullSolution();

            assertEquals(0, countEmptyCells(gameBoard), "All cells should be filled after revealing full solution");

            int size = gameBoard.getSize();
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    assertEquals(
                            solutionBoard.getValueAt(r, c),
                            gameBoard.getValueAt(r, c),
                            "Revealed board must match the solution"
                    );
                }
            }
        }

        @Test
        @DisplayName("reveal does not overwrite player's existing values")
        void revealDoesNotOverwrite_existingValues() {
            clearBoard(gameBoard);

            gameBoard.setValue(0, 0, 9);
            int beforeValue = gameBoard.getValueAt(0, 0);

            service.revealSolutionCellValue();

            int afterValue = gameBoard.getValueAt(0, 0);

            assertEquals(beforeValue, afterValue,
                    "Reveal must not overwrite manually entered player values");
        }
    }




    @Nested
    @DisplayName("Accessors")
    class AccessorTests {

        private GameService service;
        private SudokuBoard gameBoard;
        private SudokuBoard solutionBoard;

        @BeforeEach
        void setUp() {
            service = new GameService(new Validator());
            service.buildPlayableGame(9, Difficulty.EASY);

            gameBoard = service.getGameBoard();
            solutionBoard = service.getSolutionBoard();
        }

        @Test
        @DisplayName("returns correct cell values")
        void getCellValue_returnsExpected() {
            int row = 0;
            int col = 0;

            int expected = gameBoard.getValueAt(row, col);
            int actual = service.getCellValue(row, col);

            assertEquals(expected, actual,
                    "getCellValue() must return the value stored in the playable board");
        }

        @Test
        @DisplayName("returns correct subgrid dimensions")
        void getSubgridDimensions_returnsExpected() {
            int[] expected = gameBoard.getSubgridDimensions();
            int[] actual = service.getSubgridDimensions();

            assertArrayEquals(expected, actual,
                    "Returned subgrid dimensions must match playable board dimensions");
        }

        @Test
        @DisplayName("returns playable game board reference")
        void getGameBoard_returnsPlayableBoard() {
            SudokuBoard returned = service.getGameBoard();

            assertSame(gameBoard, returned,
                    "getGameBoard() must return the same instance stored in service");
        }

        @Test
        @DisplayName("returns solution board reference")
        void getSolutionBoard_returnsSolutionBoard() {
            SudokuBoard returned = service.getSolutionBoard();

            assertSame(solutionBoard, returned,
                    "getSolutionBoard() must return the same solved board used internally");
        }
    }



    @Nested
    @DisplayName("CellViewListener")
    class CellViewListenerTests {

        private GameService service;
        private SudokuBoard board;

        @BeforeEach
        void setUp() {
            service = new GameService(new Validator());
            service.buildPlayableGame(4, Difficulty.EASY);
            board = service.getGameBoard();
        }

        @Test
        @DisplayName("is not invoked on rejected update")
        void listenerNotInvoked_onRejectedUpdate() {
            final int[] callCount = {0};

            service.setCellViewListener((r, c, v) -> callCount[0]++);

            // Fill cell so update will be rejected
            board.setValue(0, 0, 1);

            boolean result = service.setValue(0, 0, 2);

            assertFalse(result, "Update must be rejected");
            assertEquals(0, callCount[0],
                    "Listener must NOT be invoked when update is rejected");
        }

        @Test
        @DisplayName("allows replacing listener with another one")
        void canReplaceListener() {
            clearBoard(board);

            final int[] firstCount = {0};
            final int[] secondCount = {0};

            service.setCellViewListener((r, c, v) -> firstCount[0]++);

            service.setCellViewListener((r, c, v) -> secondCount[0]++);

            service.setValue(0, 1, 1);

            assertEquals(0, firstCount[0], "Old listener must no longer receive events");
            assertEquals(1, secondCount[0], "New listener must receive update");
        }

        @Test
        @DisplayName("is replaced by no-op listener when null")
        void nullListener_replacedByNoOp() {
            clearBoard(board);
            // Replace with null → becomes no-op listener internally
            service.setCellViewListener(null);

            assertDoesNotThrow(() -> service.setValue(0, 0, 1));

            assertEquals(1, board.getValueAt(0, 0),
                    "Setting a value must still work when listener is null");
        }
    }

    // Helpers

    private Point findEmptyCell(SudokuBoard board) {
        int size = board.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board.getValueAt(r, c) == EMPTY_CELL) return new Point(r, c);
            }
        }
        throw new IllegalStateException("No empty cells found");
    }

    private Point findFilledCell(SudokuBoard board) {
        int size = board.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board.getValueAt(r, c) != EMPTY_CELL) return new Point(r, c);
            }
        }
        throw new IllegalStateException("No filled cells found");
    }

    private void clearBoard(SudokuBoard board) {
        int size = board.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board.setValue(r, c, EMPTY_CELL);
            }
        }
    }

    private int countEmptyCells(SudokuBoard board) {
        return countCellsMatching(board, true);
    }

    private int countFilledCells(SudokuBoard board) {
        return countCellsMatching(board, false);
    }


    private int countCellsMatching(SudokuBoard board, boolean empty) {
        int size = board.getSize();
        int count = 0;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                boolean isEmpty = board.getValueAt(r, c) == EMPTY_CELL;
                if (isEmpty == empty) {
                    count++;
                }
            }
        }
        return count;
    }
}
