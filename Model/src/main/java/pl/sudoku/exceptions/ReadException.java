package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class ReadException extends DaoException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public ReadException(String message, Throwable cause) {
        super(message, cause);

    }
}
