package pl.sudoku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.exceptions.*;

import java.sql.*;


public class JdbcSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {

    private final Connection connection;

    private final String boardName;


    public JdbcSudokuBoardDao(String jdbcUrl, String boardName) throws DataBaseProcessingException {
        try {
            this.connection = DriverManager.getConnection(jdbcUrl);
            createTablesIfNotExist();
        } catch (SQLException e) {
            DataBaseProcessingException dataBaseProcessingException
                    = new DataBaseProcessingException("dataBaseError", e.getCause());
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(dataBaseProcessingException.getLocalizedMessage(),dataBaseProcessingException);
            throw dataBaseProcessingException;
        }
        this.boardName = boardName;
    }

    private void createTablesIfNotExist() throws DataBaseProcessingException {
        try (Statement statement = connection.createStatement()) {
            connection.setAutoCommit(false);
            // check if SudokuBoard table already exists
            ResultSet resultSet = connection.getMetaData().getTables(
                    null, null,
                            "SUDOKUBOARDS", new String[]{"TABLE"});
            if (!resultSet.next()) {
                //  if not exists, create SudokuBoard table
                statement.executeUpdate("CREATE TABLE SudokuBoards "
                        + "(id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "name VARCHAR(255) NOT NULL)");
            }

            // check if SudokuCells table already exists
            resultSet = connection.getMetaData().getTables(
                    null, null,
                            "SUDOKUCELLS", new String[]{"TABLE"});
            if (!resultSet.next()) {
                //  if not exists, create SudokuCells table
                statement.executeUpdate("CREATE TABLE SudokuCells "
                        + "(id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, "
                        + "row INT NOT NULL, col INT NOT NULL, value INT NOT NULL, "
                        + "board_id INT, FOREIGN KEY (board_id) REFERENCES "
                        + "SudokuBoards(id))");

            }
        } catch (SQLException e) {
            DataBaseProcessingException dataBaseProcessingException
                    = new DataBaseProcessingException("dataBaseError", e.getCause());
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(dataBaseProcessingException.getLocalizedMessage(),dataBaseProcessingException);
            throw dataBaseProcessingException;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                DataBaseProcessingException dataBaseProcessingException
                        = new DataBaseProcessingException("saveFailed", e.getCause());
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.error(dataBaseProcessingException.getLocalizedMessage(), dataBaseProcessingException);
                throw dataBaseProcessingException;
            }
        }
    }


    private boolean isBoardExists(String boardName) throws DataBaseProcessingException {
        String query = "SELECT COUNT(*) FROM SudokuBoards WHERE name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, boardName);
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
            return count > 0;
        } catch (SQLException e) {
            DataBaseProcessingException dataBaseProcessingException
                    = new DataBaseProcessingException("dataBaseError", e.getCause());
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(dataBaseProcessingException.getLocalizedMessage(),dataBaseProcessingException);
            throw dataBaseProcessingException;
        }
    }


    private SudokuBoard loadBoard(int boardId) throws SQLException {
        BacktrackingSudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard board = new SudokuBoard(solver);
        try (PreparedStatement selectCells = connection.prepareStatement(
                "SELECT row, col, value FROM SudokuCells WHERE board_id = ?")) {
            selectCells.setInt(1, boardId);

            try (ResultSet resultSet = selectCells.executeQuery()) {
                while (resultSet.next()) {
                    int row = resultSet.getInt("row");
                    int col = resultSet.getInt("col");
                    int value = resultSet.getInt("value");
                    board.set(row, col, value);
                }
            }
        } catch (SQLException e) {
            LoadDataBaseException loadDataBaseException = new LoadDataBaseException("loadFailed", e.getCause());
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(loadDataBaseException.getLocalizedMessage(),loadDataBaseException);
            throw loadDataBaseException;
        }
        return board;
    }

    @Override
    public void close() throws CloseDataBaseException {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            CloseDataBaseException closeDataBaseException = new CloseDataBaseException("closeError", e.getCause());
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(closeDataBaseException.getLocalizedMessage(),closeDataBaseException);
            throw closeDataBaseException;
        }
    }

    @Override
    public SudokuBoard read() throws ReadException, NoSudokuClassException, LoadDataBaseException {
        try (PreparedStatement selectBoard = connection.prepareStatement(
                "SELECT id FROM SudokuBoards WHERE name = ?")) {
            connection.setAutoCommit(false);
            selectBoard.setString(1, boardName);

            try (ResultSet resultSet = selectBoard.executeQuery()) {
                if (resultSet.next()) {
                    int boardId = resultSet.getInt("id");
                    return loadBoard(boardId);
                } else {
                    LoadDataBaseException loadDataBaseException = new LoadDataBaseException("noBoard");
                    Logger logger = LoggerFactory.getLogger(this.getClass());
                    logger.error(loadDataBaseException.getLocalizedMessage(),loadDataBaseException);
                    throw loadDataBaseException;
                }
            }
        } catch (SQLException e) {
            LoadDataBaseException loadDataBaseException = new LoadDataBaseException("loadFailed", e.getCause());
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(loadDataBaseException.getLocalizedMessage(),loadDataBaseException);
            throw loadDataBaseException;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                LoadDataBaseException loadDataBaseException = new LoadDataBaseException("saveFailed", e.getCause());
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.error(loadDataBaseException.getLocalizedMessage(), loadDataBaseException);
                throw loadDataBaseException;
            }
        }
    }

    @Override
    public void write(SudokuBoard board) throws WriteException, SaveDataBaseException {
        try {
            connection.setAutoCommit(false);

            // check if board already exists
            if (isBoardExists(boardName)) {
                SaveDataBaseException saveDataBaseException = new SaveDataBaseException("boardExist");
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.error(saveDataBaseException.getLocalizedMessage(),saveDataBaseException);
                throw saveDataBaseException;
            }
            try (PreparedStatement insertBoard = connection.prepareStatement(
                    "INSERT INTO SudokuBoards (name) VALUES (?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                insertBoard.setString(1, boardName);
                insertBoard.executeUpdate();

                try (ResultSet generatedKeys = insertBoard.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int boardId = generatedKeys.getInt(1);

                        try (PreparedStatement insertCell = connection.prepareStatement(
                                "INSERT INTO SudokuCells (row, col, value, board_id) VALUES (?, ?, ?, ?)")) {
                            for (int row = 0; row < 9; row++) {
                                for (int col = 0; col < 9; col++) {
                                    insertCell.setInt(1, row);
                                    insertCell.setInt(2, col);
                                    insertCell.setInt(3, board.get(row, col));
                                    insertCell.setInt(4, boardId);
                                    insertCell.executeUpdate();
                                }
                            }
                        }
                    } else {
                        SaveDataBaseException saveDataBaseException = new SaveDataBaseException("noId");
                        Logger logger = LoggerFactory.getLogger(this.getClass());
                        logger.error(saveDataBaseException.getLocalizedMessage(),saveDataBaseException);
                        throw saveDataBaseException;
                    }
                }
            } catch (SQLException e) {
                SaveDataBaseException saveDataBaseException = new SaveDataBaseException("saveFailed", e.getCause());
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.error(saveDataBaseException.getLocalizedMessage(),saveDataBaseException);
                throw saveDataBaseException;
            }

            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                SaveDataBaseException saveDataBaseException
                        = new SaveDataBaseException("saveFailed", rollbackException.getCause());
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.error(saveDataBaseException.getLocalizedMessage(),saveDataBaseException);
                throw saveDataBaseException;
            }
            SaveDataBaseException saveDataBaseException = new SaveDataBaseException("saveFailed", e.getCause());
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(saveDataBaseException.getLocalizedMessage(),saveDataBaseException);
            throw saveDataBaseException;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                SaveDataBaseException saveDataBaseException = new SaveDataBaseException("saveFailed", e.getCause());
                Logger logger = LoggerFactory.getLogger(this.getClass());
                logger.error(saveDataBaseException.getLocalizedMessage(),saveDataBaseException);
                throw saveDataBaseException;
            }
        }


    }
}
