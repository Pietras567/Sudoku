package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import pl.sudoku.exceptions.AssertionFailed;
import pl.sudoku.exceptions.CloneFailedException;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class implementing observer interface, responsible for optional verification of user input.
 */
public class Listener implements Observer, Serializable, Cloneable {

    private boolean verification = false;

    public boolean isVerification() {
        return verification;
    }

    public void setVerification(boolean verification) {
        this.verification = verification;
    }

    /**
     * Method called when a change in value of the field is made. If new value is invalid, change is reversed.
     * @param field field from board
     * @param oldValue old field value, used to revert the change if it is not in compliance with sudoku rules
     * @param x row number of field
     * @param y column number of field
     * @param result assessment of the correctness of operation by <code>SudokuBoard</code>
     */
    @Override
    public void update(SudokuField field, int x, int y, int oldValue, boolean result) {
        if (isVerification() && !result) {
            field.setFieldValue(oldValue);
        } else {
            final Logger logger = LoggerFactory.getLogger(Listener.class);
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String from = bundle.getString("changeFieldFrom");
            String to = bundle.getString("changeFieldTo");
            String changedfield = bundle.getString("field");
            logger.info(changedfield + " " + field.toString() + " x=" + x + " y=" + y + " " + to
                    + field.getFieldValue() + " " + from + oldValue);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Listener listener = (Listener) o;

        return new EqualsBuilder().append(verification, listener.verification).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(15, 35).append(verification).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("verification", verification)
                .toString();
    }

    @Override
    public Listener clone() throws CloneFailedException {
        try {
            return (Listener) super.clone();
        } catch (CloneNotSupportedException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("assertionFailed");


            throw new CloneFailedException(message,e);
        }
    }
}
