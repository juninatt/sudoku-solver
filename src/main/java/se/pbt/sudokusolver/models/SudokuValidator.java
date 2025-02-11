package se.pbt.sudokusolver.models;

import java.util.HashSet;
import java.util.stream.IntStream;

public class SudokuValidator {

    public static boolean validateBoard(SudokuBoard board) {
        int size = board.getSize();
        int sectionSize = (int) Math.sqrt(size);

        boolean rowsAndColumnsValid = IntStream.range(0, size)
                .allMatch(i -> isValidRow(board, i) && isValidColumn(board, i));

        boolean sectionsValid = IntStream.range(0, size)
                .boxed()
                .flatMap(row -> IntStream.range(0, size)
                        .filter(col -> row % sectionSize == 0 && col % sectionSize == 0)
                        .mapToObj(col -> isValidSection(board, row, col, sectionSize)))
                .allMatch(Boolean::booleanValue);

        if (rowsAndColumnsValid && sectionsValid) {
            System.out.println("WINNER ! ! !");
            return true;
        } else {
            System.out.println("LOOSER ! ! !");
            return false;
        }
    }

    private static boolean isValidRow(SudokuBoard board, int row) {
        return IntStream.range(0, board.getSize())
                .map(col -> board.getValue(row, col))
                .filter(num -> num != 0)
                .allMatch(new HashSet<>()::add);
    }

    private static boolean isValidColumn(SudokuBoard board, int col) {
        return IntStream.range(0, board.getSize())
                .map(row -> board.getValue(row, col))
                .filter(num -> num != 0)
                .allMatch(new HashSet<>()::add);
    }

    private static boolean isValidSection(SudokuBoard board, int startRow, int startCol, int sectionSize) {
        return IntStream.range(0, sectionSize)
                .boxed()
                .flatMap(row -> IntStream.range(0, sectionSize)
                        .mapToObj(col -> board.getValue(startRow + row, startCol + col)))
                .filter(num -> num != 0)
                .allMatch(new HashSet<>()::add);
    }
}