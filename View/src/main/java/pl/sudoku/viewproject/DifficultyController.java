package pl.sudoku.viewproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.Difficulty;
import pl.sudoku.exceptions.DaoException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DifficultyController {

    @FXML
    private Label welcomeText;

    @FXML
    public Label authorsLabel;

    private FXMLLoader boardLoader  = new FXMLLoader(SudokuApplication.class.getResource("sudokuBoard.fxml"), ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle",Locale.getDefault()));
    private FXMLLoader settingsLoader  = new FXMLLoader(SudokuApplication.class.getResource("settingsMenu.fxml"), ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle", Locale.getDefault()));

    private ResourceBundle propBundle = ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle",Locale.getDefault());
    private ResourceBundle authBundle = ResourceBundle.getBundle("pl.sudoku.viewproject.AuthorsBundle",Locale.getDefault());


    @FXML
    public void onEasyButtonClick() throws DaoException {
        Parent root = null;
        try {
            root = boardLoader.load();
        } catch (IOException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("daoErr");
            DaoException exception = new DaoException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message, exception);
            throw exception;
        }
        SudokuController controller = boardLoader.getController();
        welcomeText.getScene().setRoot(root);
        controller.startGame(Difficulty.EASY);

    }

    public void onMediumButtonClick() throws DaoException {
        Parent root = null;
        try {
            root = boardLoader.load();
        } catch (IOException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("daoErr");
            DaoException exception = new DaoException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message, exception);
            throw exception;
        }
        SudokuController controller = boardLoader.getController();
        welcomeText.getScene().setRoot(root);
        controller.startGame(Difficulty.MEDIUM);
    }

    public void onHardButtonClick() throws DaoException {
        Parent root = null;
        try {
            root = boardLoader.load();
        } catch (IOException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("daoErr");
            DaoException exception = new DaoException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message, exception);
            throw exception;
        }
        SudokuController controller = boardLoader.getController();
        welcomeText.getScene().setRoot(root);
        controller.startGame(Difficulty.HARD);
    }

    public void onSettingsButtonClick(ActionEvent event) throws DaoException {
        Parent root = null;
        try {
            root = settingsLoader.load();
        } catch (IOException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("daoErr");
            DaoException exception = new DaoException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message, exception);
            throw exception;
        }
        welcomeText.getScene().setRoot(root);
    }

    public void initialize() { //method called during initialisation of controller
        authorsLabel.setText(propBundle.getString("authorsLabel")+": \n"+ authBundle.getString("GJ")+ "\n"+
                authBundle.getString("PJ"));
    }
}