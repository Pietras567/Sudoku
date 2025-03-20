package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.exceptions.CloneFailedException;
import pl.sudoku.exceptions.NullFieldException;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;



/**
 * Represents a part of the sudoku board - a field.
 */
public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
    private int value = 0;

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int newValue) {
        if (newValue >= 0 && newValue < 10) {
            int oldValue = this.value;
            this.value = newValue;
            this.pcs.firePropertyChange("FieldValue", oldValue, newValue);
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

        SudokuField that = (SudokuField) o;

        return new EqualsBuilder().append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("value", value)
                .toString();
    }

    @Override
    public SudokuField clone() throws CloneFailedException {
        SudokuField clone;
        try {
            clone = (SudokuField) super.clone();
        } catch (CloneNotSupportedException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("cloneFailed");
            CloneFailedException cloneFailedException = new CloneFailedException(message, e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(e.getMessage(),cloneFailedException);
            throw cloneFailedException;
        }
        clone.value = this.value;
        return clone;
    }

    @Override
    public int compareTo(SudokuField o) {
        try {
            if (o == null) {
                throw new NullPointerException();
            }
        } catch (NullPointerException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("nullObject");
            NullFieldException nullFieldException = new NullFieldException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message,nullFieldException);
            throw nullFieldException;
        }
        return Integer.compare(getFieldValue(),o.getFieldValue());
    }
}
