package pl.sudoku;

/**
 * Interface for different sudoku solving algorithms.
 */
public interface SudokuSolver {
    /**
     * Filling the board with an appropriate algorithm.
     * @param board <code>SudokuBoard</code> object containing 9x9 structure of fields
     */
    void solve(SudokuBoard board);
}
