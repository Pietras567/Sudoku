package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.sudoku.exceptions.CloneFailedException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Implements validation, cloning and comparison methods for parts.
 */
public abstract class SudokuPart implements Cloneable {
    private List<SudokuField> fields = Arrays.asList(new SudokuField[9]);

    /**
     * Creates part of the sudoku board.
     * @param fields list of 9 <code>SudokuField</code> objects
     */
    public SudokuPart(List<SudokuField> fields) {
            this.fields = fields;
    }

    /**
     * Verifies if field values in element are unique.
     *
     * @return <code>true</code> if values are unique <code>false</code> otherwise
     */
    public boolean verify() {
        int number = 0;
        for (int i = 0; i < 8; i++) {
            number = fields.get(i).getFieldValue();
            if (number == 0) {
                continue;
            }
            for (int j = 0; j < 9; j++) {
                if (i != j && fields.get(j).getFieldValue() == number) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        SudokuPart that = (SudokuPart) o;

        return new EqualsBuilder().append(fields, that.fields).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(21, 43).append(fields).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("fields", fields)
                .toString();
    }


    @Override
    public SudokuPart clone() throws CloneFailedException {
        SudokuPart clone;
        try {
            clone = (SudokuPart) super.clone();
        } catch (CloneNotSupportedException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("cloneFailed");
            throw new CloneFailedException(message, e);
        }
        clone.fields = Arrays.asList(new SudokuField[9]);

        for (int i = 0; i < fields.size(); i++) {
            clone.fields.set(i,new SudokuField());
            clone.fields.get(i).setFieldValue(fields.get(i).getFieldValue());
        }
        return clone;
    }
}
