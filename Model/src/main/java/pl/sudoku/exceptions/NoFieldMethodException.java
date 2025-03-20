package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class NoFieldMethodException extends SudokuException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public NoFieldMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
