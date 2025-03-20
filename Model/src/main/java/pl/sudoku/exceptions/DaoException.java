package pl.sudoku.exceptions;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DaoException extends IOException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }
}
