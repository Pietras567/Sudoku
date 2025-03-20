package pl.sudoku;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.exceptions.CloneFailedException;

import java.io.Serializable;
import java.security.SecureRandom;
import java.util.*;

/**
 * Utilizes backtracking algorithm to fill the board.
 */
public class BacktrackingSudokuSolver implements SudokuSolver, Serializable, Cloneable {

    /**
     * Checks if number can be correctly placed on a field according to sudoku rules.
     *
     * @param number verified number.
     * @param x row number.
     * @param y column number.
     * @return [boolean] <code>true</code> if a number can be correctly placed on board, <code>false</code> otherwise.
     */
    private boolean isValidNumber(SudokuBoard board,int number, int x, int y) {
        //checking rows or columns for duplicate
        for (int k = 0; k < 9; k++) {
            if (k != y && number == board.get(x, k) // checking rows
                    || k != x && number == board.get(k, y)) { //checking columns
                return false;
            }
        }

        int boxX = x - (x % 3); //first coordinates of 3x3 square, modulo 3
        int boxY = y - (y % 3);
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 3; l++) {
                if (boxX + k == x && boxY + l == y) {
                    continue;
                }
                if (number == board.get(k + boxX, l + boxY)) {
                    return false;
                }
            }
        }
        // if a number was not found in the box, it can be placed on board
        return true;
    }

    /**
     * Fills sudoku board utilizing backtracking algorithm.
     * @param board board object to fill
     * @param x     row number
     * @param y     column number
     * @return [boolean] <code>true</code> if a number was successfully placed or board is filled, <code>false</code>
     *                                                                      if no number from range 1-9 can be placed
     */
    private boolean backtracking(SudokuBoard board, int x, int y) {
        Random rand = new SecureRandom();
        Set<Integer> generatedNumbers = new HashSet<>();
        int randomNumber;
        boolean isInSet; //flag to check if number was previously generated
        while (true) {
            do {
                isInSet = false;
                randomNumber = rand.nextInt(9) + 1; // [1-9];
                if (generatedNumbers.size() == 9) {
                    board.set(x,y,0);
                    return false;
                }
                if (generatedNumbers.contains(randomNumber)) {
                    isInSet = true;
                }
                generatedNumbers.add(randomNumber);
            } while (isInSet || !isValidNumber(board, randomNumber, x, y));
            // until number is valid, and was not generated previously
            board.set(x, y, randomNumber);
            int nextX = x;
            int nextY = y + 1;
            if (nextY == 9) { //change row
                nextX += 1;
                nextY = 0;
            }
            if (nextX == 9) {
                return true;
            }
            // if false is returned, backtracks to previous call (previous field) and repeats the process
            if (backtracking(board, nextX, nextY)) {
                return true;
            }
        }
    }

    /**
     * Calls backtracking method to fill the board.
     * @param board object <code>SudokuBoard</code> containing 9x9 board to fill
     * */
    @Override
    public void solve(SudokuBoard board) {
        backtracking(board, 0, 0);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .toString();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return new EqualsBuilder().isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 33).toHashCode();
    }


    @Override
    public BacktrackingSudokuSolver clone() throws CloneFailedException {
        try {
            return (BacktrackingSudokuSolver) super.clone();
        } catch (CloneNotSupportedException e) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.getDefault());
            String message = bundle.getString("cloneFailed");
            CloneFailedException cloneFailedException = new CloneFailedException(message,e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(message,cloneFailedException);
            throw new CloneFailedException(message, e);
        }
    }
}
