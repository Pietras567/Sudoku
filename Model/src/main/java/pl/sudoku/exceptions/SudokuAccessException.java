package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class SudokuAccessException extends SudokuException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public SudokuAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
