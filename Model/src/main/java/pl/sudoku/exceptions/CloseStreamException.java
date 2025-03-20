package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class CloseStreamException extends DaoException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public CloseStreamException(String message, Throwable cause) {
        super(message, cause);
    }
}
