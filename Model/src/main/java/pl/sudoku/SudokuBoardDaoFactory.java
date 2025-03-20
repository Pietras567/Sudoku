package pl.sudoku;

import pl.sudoku.exceptions.DaoException;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Returns appropriate DAO (Data Access Object) for managing the file or database.
 */
public class SudokuBoardDaoFactory {

    public Dao<SudokuBoard> getFileDao(String fileName) {
        return new FileSudokuBoardDao(fileName);
    }

    public static Dao<SudokuBoard> createJdbcDao(String jdbcUrl, String boardName) throws DaoException {
        try {
            return new JdbcSudokuBoardDao(jdbcUrl, boardName);
        } catch (Exception e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("creatingErrorJbdc");
            throw new DaoException(message, e.getCause());
        }
    }
}
