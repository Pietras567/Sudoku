package pl.sudoku;

/**
 * Interfejs obserwowanego obiektu.
 */
public interface Subject {
    void attachObserver(Observer observer);

    void detachObserver(Observer observer);

}
