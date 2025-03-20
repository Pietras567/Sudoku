package pl.sudoku.exceptions;

import java.util.Locale;
import java.util.ResourceBundle;

public class NoSudokuClassException extends ClassNotFoundException {
    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public NoSudokuClassException(String s, Throwable ex) {
        super(s, ex);
    }
}
