package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.sudoku.exceptions.CloneFailedException;

import java.util.List;

/**
 * Represents a part of the sudoku board - a 3x3 square.
 */
public class SudokuBox extends SudokuPart implements Cloneable {
    public SudokuBox(List<SudokuField> fields) {
        super(fields);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(27, 49).appendSuper(super.hashCode()).toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return new EqualsBuilder().appendSuper(super.equals(o)).isEquals();
    }

    @Override
    public SudokuBox clone() throws CloneFailedException {
        return (SudokuBox) super.clone();
    }
}
