package pl.sudoku.exceptions;


import java.util.Locale;
import java.util.ResourceBundle;

public class WriteException extends DaoException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public WriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
