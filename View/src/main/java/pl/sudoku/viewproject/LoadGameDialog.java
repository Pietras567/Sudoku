package pl.sudoku.viewproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pl.sudoku.Dao;
import pl.sudoku.SudokuBoard;
import pl.sudoku.SudokuBoardDaoFactory;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoadGameDialog {

    @FXML
    public Label infoLabel;

    @FXML
    public TextField inputText;

    @FXML
    public Label resultLabel;

    public Button confirmButton;
    public Button returnButton;

    private ResourceBundle bundle = ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle",Locale.getDefault());


    //Stage and Scene for returning to previous window
    private Scene prevScene;

    private Stage prevStage;

    private SudokuController sudokuController;

    private SudokuBoardDaoFactory factory = new SudokuBoardDaoFactory();


    public void loadControls(ActionEvent event, SudokuController sudokuController) {
        //Save current Stage and Scene
        this.prevScene = ((Node)event.getSource()).getScene();
        this.prevStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        this.sudokuController = sudokuController;
    }

    public void setSaveLabels() {
        infoLabel.setText(bundle.getString("labelSaveInfo"));
        confirmButton.setText(bundle.getString("saveConfBtn"));
    }

    public void setLoadLabels() {
        infoLabel.setText(bundle.getString("labelLoadInfo"));
        confirmButton.setText(bundle.getString("loadConfBtn"));
    }

    public void loadGame(String input) {
        SudokuBoard sudokuBoard = null;
        String jdbcUrl = "jdbc:derby:mydatabase;create=true";
        try (Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.createJdbcDao(jdbcUrl, input))
        {
            sudokuBoard = sudokuBoardDao.read();
        }catch (Exception e) {
            resultLabel.setText(bundle.getString("errOcc")+": "+e.getLocalizedMessage());
            return;
        }
        //Setting newly read SudokuBoard using sudokuController
        sudokuController.setBoard(sudokuBoard);
        sudokuController.displayBoard();
        resultLabel.setText(bundle.getString("loadSuccess"));
    }

    public void saveGame(String input) {
        SudokuBoard sudokuBoard = sudokuController.getSudokuBoard();
        String jdbcUrl = "jdbc:derby:mydatabase;create=true";
        try (Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.createJdbcDao(jdbcUrl, input))
        {
            sudokuBoardDao.write(sudokuBoard);
        } catch (Exception e) {
            resultLabel.setText(bundle.getString("errOcc")+": "+e.getLocalizedMessage());
            return;
        }
        resultLabel.setText(bundle.getString("saveSuccess"));
    }

    public void confirmButtonClick() {
        String input = inputText.getCharacters().toString();
        if(Objects.equals(infoLabel.getText(), bundle.getString("labelLoadInfo"))) {
            loadGame(input);
        }
        else {
            saveGame(input);
        }
    }

    public void returnButtonClick(ActionEvent event) {
        prevStage.setScene(prevScene);
        prevStage.show();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        resultLabel.setText("");
        stage.close();
    }
}
