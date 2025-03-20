package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pl.sudoku.exceptions.CloneFailedException;

import java.io.Serializable;
import java.util.*;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Klasa przechowujaca plansze sudoku i metody ją wypełniające.
 *
 * @author Grzegorz Janasek
 * @author Piotr Janiszek
 */
public class SudokuBoard implements Subject, Serializable, Cloneable {

    private SudokuField[][] board = new SudokuField[9][9];
    private final SudokuSolver solver;

    private Set<Observer> observers = new HashSet<>();


    /**
     * Konstruktor implementujący wstrzyknięcie obiektu <code>SudokuSolver</code>.
     *
     * @param solver wstrzykiwany obiekt typu <code>SudokuSolver</code>
     */
    public SudokuBoard(SudokuSolver solver) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = new SudokuField();
            }

        }
        this.solver = solver;
    }

    /**
     * Zwraca wartość w tablicy dla określonego pola.
     *
     * @param x numer wiersza.
     * @param y numer kolumny.
     * @return wartość w podanej komórce.
     */
    public int get(int x, int y) {
        return board[x][y].getFieldValue();
    }

    /**
     * Wstawia wartość na określone pole w tablicy.
     *
     * @param x     numer wiersza.
     * @param y     numer kolumny.
     * @param value wartosc wstawiana
     */
    public void set(int x, int y, int value) {
        int oldValue = this.board[x][y].getFieldValue();
        this.board[x][y].setFieldValue(value);
        notifyObservers(this.board[x][y],x,y, oldValue, checkField(x, y));
    }

    /**
     * Wywołuje algorytm wypełniający tablicę.
     */
    public void solveGame() {
        solver.solve(this);
        checkBoard();
    }

    private boolean checkRows() {
        //sprawdzanie wszystkich wierszy na planszy
        for (int i = 0; i < 9; i++) {
            if (!getRow(i).verify()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkColumns() {
        //sprawdzanie wszystkich kolumn na planszy
        for (int i = 0; i < 9; i++) {
            if (!getColumn(i).verify()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkBox() {
        //sprawdzenie wszystkich kwadratów 3x3 na planszy
        for (int i = 0; i < 9; i += 3) {
            for (int j = 0; j < 9; j += 3) {
                if (!getBox(i, j).verify()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkBoard() {
        return checkRows() && checkColumns() && checkBox();
    }

    /**
     * Zwraca wiersz w tablicy dla określonego pola.
     *
     * @param x numer wiersza.
     * @return lista z polami dla danego wiersza
     */
    public SudokuRow getRow(int x) {
        List<SudokuField> fields = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            fields.set(i, board[x][i]);
        }
        return new SudokuRow(fields);
    }

    /**
     * Zwraca kolumnę w tablicy dla określonego pola.
     *
     * @param y numer kolumny.
     * @return tablica z polami dla danej kolumny
     */
    public SudokuColumn getColumn(int y) {
        List<SudokuField> fields = Arrays.asList(new SudokuField[9]);
        for (int i = 0; i < 9; i++) {
            fields.set(i, board[i][y]);
        }
        return new SudokuColumn(fields);
    }

    /**
     * Zwraca kwadrat 3x3 w tablicy dla określonego pola.
     *
     * @param x numer wiersza.
     * @param y numer kolumny.
     * @return tablica z polami dla danego kwadratu 3x3
     */
    public SudokuBox getBox(int x, int y) {
        List<SudokuField> fields = Arrays.asList(new SudokuField[9]);
        int boxX = x - (x % 3);
        int boxY = y - (y % 3);
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                fields.set(index, board[boxX + i][boxY + j]);
                index++;
            }
        }
        return new SudokuBox(fields);
    }

    private boolean checkField(int x, int y) {
        return getRow(x).verify() && getColumn(y).verify() && getBox(x, y).verify();
    }

    /**
     * Dodaje obserwatora do listy obserwatorów.
     * @param observer obiekt klasy implementującej interfejs obserwatora
     *
     */
    @Override
    public void attachObserver(Observer observer) {
        observers.add(observer);

    }

    /**
     * Uruchamia mechanizm usunięcia obserwatorów z listy.
     *
     * @param observer obiekt obserwatora do usunięcia z listy obserwatorów
     */
    @Override
    public void detachObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Uruchamia mechanizm powiadamiania obserwatorów o zmianie wartości pola.
     *
     * @param field pole na planszy
     * @param oldValue stara wartość pola, używana do cofnięcia ustawienia wartości w przypadku wprowadzenie niezgodnej
     *                 z zasadami gry wartości.
     * @param result sprawdzenie poprawności wstawienia liczby na planszę
     */
    private void notifyObservers(SudokuField field, int x, int y, int oldValue, boolean result) {
        for (Observer observer :
                observers) {
            observer.update(field, x, y, oldValue, result);

        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SudokuBoard board1 = (SudokuBoard) o;

        return new EqualsBuilder()
                .append(board, board1.board)
                .append(solver, board1.solver)
                .append(observers, board1.observers)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(19, 41)
                .append(board)
                .append(solver)
                .append(observers)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("board", board)
                .append("solver", solver)
                .append("observers", observers)
                .toString();
    }

    @Override
    public SudokuBoard clone() throws CloneFailedException {
        SudokuBoard clone = null;
        try {
            clone = (SudokuBoard) super.clone();
        } catch (CloneNotSupportedException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("cloneFailed");
            throw new CloneFailedException(message, e);
        }
        clone.board = new SudokuField[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                clone.board[i][j] = new SudokuField();
                clone.board[i][j].setFieldValue(board[i][j].getFieldValue());
            }
        }
        clone.observers = new HashSet<Observer>();
        clone.observers.addAll(observers);
        return clone;
    }

}