package se.pbt.sudokusolver.builders;

import se.pbt.sudokusolver.builders.helpers.SolutionGenerator;
import se.pbt.sudokusolver.builders.helpers.UniquenessChecker;
import se.pbt.sudokusolver.models.Difficulty;
import se.pbt.sudokusolver.models.SudokuBoard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class generates a fully solved Sudoku board and removes numbers based on
 * the selected difficulty level. It ensures the puzzle remains solvable with only one solution.
 * The class relies on {@link SolutionGenerator} for solving and checking solution uniqueness.
 */
public class SudokuBuilder {

    int size;
    Difficulty difficulty;
    UniquenessChecker uniquenessChecker;
    SolutionGenerator solutionGenerator;

    public SudokuBuilder(int size, Difficulty difficulty, UniquenessChecker uniquenessChecker, SolutionGenerator solutionGenerator) {
        this.size = size;
        this.difficulty = difficulty;
        this.uniquenessChecker = uniquenessChecker;
        this.solutionGenerator = solutionGenerator;
    }

    /**
     * Generates a Sudoku puzzle with a unique solution based on the selected difficulty level.
     *
     * @return A {@link SudokuBoard} containing a valid Sudoku puzzle.
     */
    public SudokuBoard buildSudokuPuzzle() {
        SudokuBoard gameBoard = createSolvedBoard();

        // Additional randomization to minimize risk of similarities between puzzles
        randomizeBoard(gameBoard);

        // Remove numbers from the full sudoku puzzle solution based on difficulty
        removeValuesFromBoard(gameBoard);

        return gameBoard;
    }


    private SudokuBoard createSolvedBoard() {
        SudokuBoard board = new SudokuBoard(size);
        if (solutionGenerator.fillBoardWithSolution(board, 0, 0)) {
            return board.copy();
        } else {
            throw new IllegalStateException("Failed generating full solution.");
        }
    }

    private void removeValuesFromBoard(SudokuBoard gameBoard) {
        int valuesRemoved = 0;
        int valuesToRemove = difficulty.calculateValuesToRemove(size);

        List<Point> cellsToRemove = getCellsForValueRemoval(gameBoard.getSize(), valuesToRemove);

        for (Point p : cellsToRemove) {
            if (valuesRemoved >= valuesToRemove) break;

            int backup = gameBoard.getValueAt(p.x, p.y);
            gameBoard.setValue(p.x, p.y, 0);

            if (!uniquenessChecker.hasUniqueSolution(gameBoard)) {
                gameBoard.setValue(p.x, p.y, backup);
            } else {
                valuesRemoved++;
            }
        }
    }


    private List<Point> getCellsForValueRemoval(int size, int valuesToRemove) {
        List<Point> cells = new ArrayList<>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                cells.add(new Point(row, col));
            }
        }
        Collections.shuffle(cells);
        return cells.subList(0, valuesToRemove);
    }

    /**
     * Randomizes the board by applying a shuffled permutation to the existing numbers.
     * The function retains the structure but changes number placement while ensuring
     * that the board remains a valid Sudoku puzzle.
     */
    public static void randomizeBoard(SudokuBoard board) {
        List<Integer> mapping = IntStream.rangeClosed(1, board.getSize())
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(mapping);

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                int originalValue = board.getValueAt(row, col);
                int newValue = (originalValue != 0) ? mapping.get(originalValue - 1) : 0;
                board.setValue(row, col, newValue);
            }
        }
    }
}
