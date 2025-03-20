package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class SudokuException extends RuntimeException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public SudokuException(String message, Throwable cause) {
        super(message, cause);
    }

    public SudokuException(String message) {
        super(message);
    }
}
