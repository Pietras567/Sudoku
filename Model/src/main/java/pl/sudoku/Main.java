package pl.sudoku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Displays filled sudoku.
 */
public class Main {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(Main.class);
        String jdbcUrl = "jdbc:derby:mydatabase;create=true"; //Set database configuration
        try (Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.createJdbcDao(jdbcUrl, "SudokuBoard")) {
            // Create board
            SudokuSolver solver = new BacktrackingSudokuSolver();
            SudokuBoard sudokuBoard = new SudokuBoard(solver);
            // Fill sudoku and remove number of fields depending on difficulty level
            sudokuBoard.solveGame();
            Difficulty.EASY.clearFields(sudokuBoard);

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (sudokuBoard.get(i, j) == 0) {
                        logger.info(" " + " ");
                    } else {
                        logger.info(sudokuBoard.get(i, j) + " ");
                    }
                }
                logger.info("\n");
            }

            logger.info("\n\n\n\nBoard is saved to database and read to new instance of SudokuBoard");

            // Save board
            sudokuBoardDao.write(sudokuBoard);

            // Read board
            SudokuBoard loadedBoard = sudokuBoardDao.read();

            // Display board
            logger.info("\n\n\n\n\n");

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (loadedBoard.get(i, j) == 0) {
                        logger.info(" " + " ");
                    } else {
                        logger.info(loadedBoard.get(i, j) + " ");
                    }
                }
                logger.info("\n");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
