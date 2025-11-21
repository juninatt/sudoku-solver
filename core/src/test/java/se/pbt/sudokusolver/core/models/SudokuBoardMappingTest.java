package se.pbt.sudokusolver.core.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.pbt.sudokusolver.shared.dto.SudokuBoardDto;

import static org.junit.jupiter.api.Assertions.*;

class SudokuBoardMappingTest {

    @Test
    @DisplayName("toDto and fromDto should preserve board size and structure")
    void toDto_and_fromDto_preservesStructure() {
        SudokuBoard board = new SudokuBoard(9);
        SudokuBoardDto dto = board.toDto();
        SudokuBoard restored = SudokuBoard.fromDto(dto);

        assertEquals(board.getSize(), restored.getSize());
        assertArrayEquals(board.getSubgridDimensions(), restored.getSubgridDimensions());
    }

    @Test
    @DisplayName("toDto and fromDto should preserve cell values")
    void toDto_and_fromDto_preservesCellValues() {
        SudokuBoard board = new SudokuBoard(9);
        board.setValue(0, 0, 1);
        board.setValue(4, 4, 9);
        board.setValue(8, 8, 7);

        SudokuBoardDto dto = board.toDto();
        SudokuBoard restored = SudokuBoard.fromDto(dto);

        assertEquals(1, restored.getValueAt(0, 0));
        assertEquals(9, restored.getValueAt(4, 4));
        assertEquals(7, restored.getValueAt(8, 8));
    }
}
