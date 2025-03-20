package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class SaveDataBaseException extends DataBaseException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public SaveDataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveDataBaseException(String message) {
        super(message);
    }
}
