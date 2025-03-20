package pl.sudoku.exceptions;

import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DataBaseException extends SQLException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public DataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataBaseException(String message) {
        super(message);
    }
}
