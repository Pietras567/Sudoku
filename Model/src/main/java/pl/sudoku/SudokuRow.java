package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.sudoku.exceptions.CloneFailedException;

import java.util.List;

/**
 * Represents a part of the sudoku board - a row.
 */

public class SudokuRow extends SudokuPart implements Cloneable {

    public SudokuRow(List<SudokuField> fields) {
        super(fields);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(23, 45).appendSuper(super.hashCode()).toHashCode();
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
    public SudokuRow clone() throws CloneFailedException {
        return (SudokuRow) super.clone();
    }
}
