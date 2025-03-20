package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class NoSudokuFieldException extends SudokuException {

    private Throwable cause;

    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public NoSudokuFieldException(String s, Throwable cause) {
        super(s);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
