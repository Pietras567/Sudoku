package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class DataBaseProcessingException extends DataBaseException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    public DataBaseProcessingException(String message, Throwable cause) {
        super(message, cause);
    }


    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }
}
