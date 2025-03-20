module ModelProject {
    requires org.apache.commons.lang3;
    requires org.slf4j;
    requires java.desktop;
    requires java.sql;

    exports pl.sudoku;
    exports pl.sudoku.exceptions;
    opens pl.sudoku to pl.sudoku.viewproject;
}