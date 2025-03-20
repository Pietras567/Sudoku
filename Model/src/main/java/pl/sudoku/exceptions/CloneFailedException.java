package pl.sudoku.exceptions;


import java.util.Locale;
import java.util.ResourceBundle;

public class CloneFailedException extends CloneNotSupportedException {
    private Throwable cause;

    ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());

    @Override
    public String getLocalizedMessage() {
        return bundle.getString(getMessage());
    }

    public CloneFailedException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
