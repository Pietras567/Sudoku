package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import pl.sudoku.exceptions.CloneFailedException;

import java.util.List;

/**
 * Represents a part of the sudoku board - a column.
 */
public class SudokuColumn extends SudokuPart {

    public SudokuColumn(List<SudokuField> fields) {
        super(fields);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(25, 47).appendSuper(super.hashCode()).toHashCode();
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
    public SudokuColumn clone() throws CloneFailedException {
        return (SudokuColumn) super.clone();

    }
}
