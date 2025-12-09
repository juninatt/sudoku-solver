package se.pbt.sudokusolver.ui.viewmodel;

import javafx.scene.control.TextField;
import se.pbt.sudokusolver.game.service.GameService;
import se.pbt.sudokusolver.shared.game.PuzzleDifficulty;
import se.pbt.sudokusolver.ui.interaction.CellEventHandler;
import se.pbt.sudokusolver.ui.view.CellFactory;

import static se.pbt.sudokusolver.ui.constants.UIConstants.EMPTY_CELL;

/**
 * Serves as the connection layer between the UI and the underlying {@code SudokuBoard}.
 * Centralizes read and write operations so the UI can interact with the board safely,
 * while keeping validation and game-state updates contained within one place.
 */
public class SudokuViewModel {

    private final GameService gameService;
    private final CellEventHandler cellEventHandler;
    private final CellFactory cellFactory;

    /**
     * Sets up the ViewModel as the link between UI and game logic.
     */
    public SudokuViewModel(GameService gameService,
                           CellEventHandler cellEventHandler,
                           CellFactory cellFactory) {
        this.gameService = gameService;
        this.cellEventHandler = cellEventHandler;
        this.cellFactory = cellFactory;
    }

    /**
     * Creates a new Sudoku game and initializes the playable and solution boards.
     */
    public void createSudokuGame(int size, PuzzleDifficulty difficulty) {
        gameService.buildPlayableGame(size, difficulty);
    }

    /**
     * Creates a UI cell based on the current board state.
     * Returns either a clue cell or an editable cell.
     */
    public TextField createCell(int row, int col) {
        int value = getCellValue(row, col);
        return (value != EMPTY_CELL)
                ? cellFactory.createClueCell(value)
                : createEmptyCell(row, col);
    }

    private TextField createEmptyCell(int row, int col) {
        TextField cell = cellFactory.createBaseCell();
        cellEventHandler.attachInputHandlers(cell, row, col, this);
        return cell;
    }

    /**
     * Attempts to write a value to the board. Returns {@code true} only if the update succeeded.
     */
    public boolean setValue(int row, int col, int value) {
        return gameService.setValue(row, col, value);
    }

    /**
     * Reveals the correct value for a single empty cell.
     */
    public void revealCellFromSolution() {
        gameService.revealSolutionCellValue();
    }

    /**
     * Reveals the entire solution by filling all empty cells.
     */
    public void revealAllCellsFromSolution() {
        gameService.revealFullSolution();
    }

    /**
     * Retrieves the value stored in the underlying board at the given position.
     */
    public int getCellValue(int row, int col) {
        return gameService.getCellValue(row, col);
    }

    /**
     * Returns the subgrid dimensions used by the current Sudoku board.
     */
    public int[] getSubGridDimensions() {
        return gameService.getSubgridDimensions();
    }
}
