package pl.sudoku.viewproject;

import java.util.ListResourceBundle;

public class AuthorsBundle_pl_PL extends ListResourceBundle {

    private final Object[][] authors = {
            {"GJ", "Grzegorz Janasek"},
            {"PJ", "Piotr Janiszek"}
    };

    @Override
    protected Object[][] getContents() {
        return authors;
    }
}
