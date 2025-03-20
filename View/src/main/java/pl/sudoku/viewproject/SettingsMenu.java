package pl.sudoku.viewproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.exceptions.DaoException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsMenu {

    @FXML
    public RadioButton radio_pl;

    @FXML
    public RadioButton radio_en;
    @FXML
    public Label settingsTitle;
    @FXML
    public Button menuButton;

    private FXMLLoader menuLoader  = new FXMLLoader(SudokuApplication.class.getResource("difficultyMenu.fxml"),ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle",Locale.getDefault()));

    private FXMLLoader settingsLoader  = new FXMLLoader(SudokuApplication.class.getResource("settingsMenu.fxml"), ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle",Locale.getDefault()));
    public void setLangEN(ActionEvent event) throws DaoException {
        Locale locale = new Locale("en","EN");
        Locale.setDefault(locale);
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
        settingsTitle.getScene().setRoot(root);
    }

    public void setLangPL(ActionEvent event) throws DaoException {
        Locale locale = new Locale("pl","PL");
        Locale.setDefault(locale);
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

        settingsTitle.getScene().setRoot(root);
    }

    public void returnToMenu(ActionEvent event) throws DaoException {

        Parent root = null;
        try {
            root = menuLoader.load();
        } catch (IOException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("daoErr");
            DaoException exception = new DaoException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message, exception);
            throw exception;
        }
        settingsTitle.getScene().setRoot(root);
    }
}
