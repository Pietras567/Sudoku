package pl.sudoku.viewproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.exceptions.DaoException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SudokuApplication extends Application {
    @Override
    public void start(Stage stage) throws DaoException {

        FXMLLoader fxmlLoader = new FXMLLoader(SudokuApplication.class.getResource("difficultyMenu.fxml"), ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle", Locale.getDefault()));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            DaoException exception = new DaoException("daoErr",e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(exception.getLocalizedMessage(), exception);
            throw exception;
        }
        stage.setTitle("Sudoku");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}