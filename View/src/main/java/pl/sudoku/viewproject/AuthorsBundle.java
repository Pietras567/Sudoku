package pl.sudoku.viewproject;

import java.util.ListResourceBundle;

public class AuthorsBundle extends ListResourceBundle {

    private final Object[][] authors = {
            {"GJ", "Gregory Janasek"},
            {"PJ", "Peter Janiszek"}
    };
    @Override
    protected Object[][] getContents() {
        return authors;
    }
}
