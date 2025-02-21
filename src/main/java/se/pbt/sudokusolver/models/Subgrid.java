package se.pbt.sudokusolver.models;

/**
 * Represents a subgrid (box) within a Sudoku board.
 * Each subgrid stores a portion of the Sudoku board and ensures validity of its contained numbers.
 */
public class Subgrid {
    private final int startRow;
    private final int startCol;
    private final int rows;
    private final int cols;
    private final int[][] subgridValues;

    /**
     * Constructs a subgrid based on its position and dimensions.
     *
     * @param board  The main Sudoku board reference.
     * @param startRow The starting row index of the subgrid.
     * @param startCol The starting column index of the subgrid.
     * @param rows The number of rows in the subgrid.
     * @param cols The number of columns in the subgrid.
     */
    public Subgrid(SudokuBoard board, int startRow, int startCol, int rows, int cols) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.rows = rows;
        this.cols = cols;
        this.subgridValues = new int[rows][cols];

        initialize(board);
    }

    /**
     * Fills the subgrid with values from the parent board upon creation.
     */
    private void initialize(SudokuBoard board) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                subgridValues[r][c] = board.getValueAt(startRow + r, startCol + c);
            }
        }
    }

    /**
     * Sets a value in the subgrid and ensures synchronization with the main board.
     */
    public void setValue(int row, int col, int value) {
        int localRow = row - startRow;
        int localCol = col - startCol;
        if (localRow >= 0 && localRow < rows && localCol >= 0 && localCol < cols) {
            subgridValues[localRow][localCol] = value;
        }
    }

    /**
     * Gets a value from the subgrid at the specified position.
     */
    public int getValue(int row, int col) {
        int localRow = row - startRow;
        int localCol = col - startCol;
        return (localRow >= 0 && localRow < rows && localCol >= 0 && localCol < cols)
                ? subgridValues[localRow][localCol] : -1;
    }

    /**
     * Returns the subgrid as a 2D array.
     */
    public int[][] getSubgridValues() {
        return subgridValues;
    }
}
