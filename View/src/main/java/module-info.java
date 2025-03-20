module pl.sudoku.viewproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires ModelProject;
    requires java.desktop;
    requires org.slf4j;


    opens pl.sudoku.viewproject to javafx.fxml;
    exports pl.sudoku.viewproject;
}