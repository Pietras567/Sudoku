package pl.sudoku;

import pl.sudoku.exceptions.CloneFailedException;

public class SudokuBoardRepository {
    private final SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());

    public SudokuBoard createInstance() {
        try {
            return sudokuBoard.clone();
        } catch (CloneFailedException e) {
            return null;
        }
    }


}
