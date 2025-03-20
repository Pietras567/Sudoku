package pl.sudoku.viewproject;

import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;

import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.*;
import pl.sudoku.exceptions.DaoException;
import pl.sudoku.exceptions.NoFieldMethodException;
import pl.sudoku.exceptions.NoSudokuFieldException;
import pl.sudoku.exceptions.SudokuAccessException;

import java.io.IOException;
import java.lang.reflect.Field;

import java.util.Locale;
import java.util.ResourceBundle;


public class SudokuController {

    @FXML
    private GridPane gridPane = new GridPane();

    @FXML
    public Button saveButton;

    @FXML
    public Button loadButton;

    @FXML
    public Button checkButton;

    private SudokuBoard sudokuBoard;

    private ResourceBundle bundle = ResourceBundle.getBundle("pl.sudoku.viewproject.MyBundle",Locale.getDefault());

    private FXMLLoader dialogLoader  = new FXMLLoader(SudokuApplication.class.getResource("loadGameDialog.fxml"),bundle);
    private Stage dialogStage;
    private Scene dialogScene;

    private TextField[][] textFields = new TextField[9][9];

    SudokuField[][] reflectedBoard;



    public SudokuController() throws DaoException {
        SudokuBoardRepository repository = new SudokuBoardRepository();
        sudokuBoard = repository.createInstance();
        sudokuBoard.solveGame();
        dialogStage = new Stage();
        try {
            dialogScene = new Scene(dialogLoader.load());
        } catch (IOException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("daoErr");
            DaoException exception = new DaoException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message, exception);
            throw exception;
        }

        Listener listener = new Listener();
        listener.setVerification(false);
        sudokuBoard.attachObserver(listener);
    }

    public void initialize() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                textFields[i][j] = new TextField();
                textFields[i][j].setPrefSize(100,100);
                gridPane.add(textFields[i][j],j,i);
            }
        }
    }

    public void setBoard(SudokuBoard board) {
        this.sudokuBoard = board;
    }

    public SudokuBoard getSudokuBoard() {
        return sudokuBoard;
    }

    public void startGame(Difficulty difficulty) {
        difficulty.clearFields(sudokuBoard);
        displayBoard();
    }


    public void displayBoard() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField textField = textFields[i][j];
                Field privateBoard;
                try {
                    privateBoard = sudokuBoard.getClass().getDeclaredField("board");
                    privateBoard.setAccessible(true);
                    reflectedBoard = (SudokuField[][]) privateBoard.get(sudokuBoard);
                } catch (NoSuchFieldException e) {
                    NoSudokuFieldException exception = new NoSudokuFieldException("fieldErr",e);
                    Logger logger = LoggerFactory.getLogger(this.getClass());
                    logger.error(exception.getLocalizedMessage(), exception);
                    throw exception;
                } catch (IllegalAccessException e) {
                    SudokuAccessException exception = new SudokuAccessException("accErr",e);
                    Logger logger = LoggerFactory.getLogger(this.getClass());
                    logger.error(exception.getLocalizedMessage(), exception);
                    throw exception;
                }
                //bidirectional binding using JavaBeanPropertyBinding
                JavaBeanIntegerProperty prop;
                try {
                    prop = JavaBeanIntegerPropertyBuilder
                            .create()
                            .bean(reflectedBoard[i][j])
                            .name("FieldValue")
                            .build();
                } catch (NoSuchMethodException e) {
                    NoFieldMethodException exception = new NoFieldMethodException("noMethod",e);
                    Logger logger = LoggerFactory.getLogger(this.getClass());
                    logger.error(exception.getLocalizedMessage(), exception);
                    throw exception;
                }

                TextFormatter<Integer> textFormatter = new TextFormatter<>(new SudokuNumbersConverter(),0, new SudokuNumbersFilter());
                textFormatter.valueProperty().bindBidirectional(prop.asObject());
                textField.setTextFormatter(textFormatter);
                //fields with removed values are editable
                if (sudokuBoard.get(i,j) == 0 ) {
                    textField.setEditable(true);
                    textField.setAlignment(Pos.CENTER);

                } else {
                    textField.setText(Integer.toString(sudokuBoard.get(i,j)));
                    textField.setEditable(false);
                    textField.setAlignment(Pos.CENTER);

                }
                    if (i%3==0 && j%3==0) {
                        textField.setStyle("-fx-border-width: 4px 0px 0px 4px; -fx-border-color: black;");

                    }
                    else if(i%3==0 ) {
                        textField.setStyle("-fx-border-width: 4px 0px 0px 0px; -fx-border-color: black;");
                    }
                    else if(j%3==0) {
                        textField.setStyle("-fx-border-width: 0px 0px 0px 4px; -fx-border-color: black;");
                    }
            }
        }
    }

    public void onLoadButtonClick(ActionEvent event) {
        dialogStage.setTitle("Dialog");
        dialogStage.setScene(dialogScene);
        LoadGameDialog controller = dialogLoader.getController();
        controller.loadControls(event, this);
        controller.setLoadLabels();
        dialogStage.show();
    }

    public void onSaveButtonClick(ActionEvent event) {
        dialogStage.setTitle("Dialog");
        dialogStage.setScene(dialogScene);
        LoadGameDialog controller = dialogLoader.getController();
        controller.loadControls(event, this);
        controller.setSaveLabels();
        dialogStage.show();
    }

    public void onCheckButtonClick() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setContentText(bundle.getString("alertNotWin"));
        alert.setHeaderText(null);
        alert.setGraphic(null);


        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if(sudokuBoard.get(i,j) == 0) {
                    alert.showAndWait();
                    return;
                }
            }
        }
        if(sudokuBoard.checkBoard()) {
            Alert winAlert = new Alert(Alert.AlertType.INFORMATION);
            winAlert.setTitle("");
            winAlert.setContentText(bundle.getString("alertWin"));
            winAlert.setHeaderText(null);
            winAlert.setGraphic(null);
            winAlert.showAndWait();
        } else {
            alert.showAndWait();
        }

    }

}
