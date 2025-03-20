package pl.sudoku;

/**
 * Observer interface.
 */
public interface Observer {

    /**
     * Method called if change in observable object is detected.
     * @param field <code>SudokuField</code> object from board
     * @param x row number
     * @param y column number
     * @param oldValue value of field before change
     * @param result operation validation
     */
    void update(SudokuField field, int x, int y, int oldValue, boolean result);
}
