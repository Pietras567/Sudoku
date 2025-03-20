package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class LoadDataBaseException extends DataBaseException {

    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public LoadDataBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoadDataBaseException(String message) {
        super(message);
    }


}
