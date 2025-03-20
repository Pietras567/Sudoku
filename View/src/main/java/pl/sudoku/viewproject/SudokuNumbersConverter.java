package pl.sudoku.viewproject;

import javafx.util.converter.IntegerStringConverter;

public class SudokuNumbersConverter extends IntegerStringConverter {
    @Override
    public Integer fromString(String s) {
        if (s.isEmpty() || s.equals(" ")) {
            return 0;
        }
        return super.fromString(s);
    }

    @Override
    public String toString(Integer integer) {
        if (integer == 0){
            return "";
        }
        return super.toString(integer);
    }
}
