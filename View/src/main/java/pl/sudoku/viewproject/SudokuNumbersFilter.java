package pl.sudoku.viewproject;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;

public class SudokuNumbersFilter implements UnaryOperator<TextFormatter.Change> {

    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        if(change.getControlNewText().matches("^[1-9 ]$")) {
            return change;
        }
        return null;
    }
}
