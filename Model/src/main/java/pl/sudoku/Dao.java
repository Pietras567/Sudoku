package pl.sudoku;


import pl.sudoku.exceptions.*;

/**
 * Interface specifying behavior for DAO components.
 * @param <T> type of saved object
 */
public interface Dao<T> extends AutoCloseable {

    T read() throws ReadException, NoSudokuClassException, LoadDataBaseException;

    void write(T obj) throws WriteException, SaveDataBaseException;


}
