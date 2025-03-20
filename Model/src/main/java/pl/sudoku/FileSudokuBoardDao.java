package pl.sudoku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sudoku.exceptions.*;

import java.io.*;


public class FileSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {

    private final String fileName;

    private ObjectInputStream inputStream = null;

    private ObjectOutputStream outputStream = null;

    public FileSudokuBoardDao(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Reads and returns saved <code>SudokuBoard</code> object from file.
     *
     * @return <code>SudokuBoard</code> object read from file
     */
    @Override
    public SudokuBoard read() throws ReadException, NoSudokuClassException {
        SudokuBoard board;
        try {
            FileInputStream fileStream = new FileInputStream(fileName);
            inputStream = new ObjectInputStream(fileStream);
            board = (SudokuBoard) inputStream.readObject();
        } catch (IOException e) {
            ReadException readException = new ReadException("readError",e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(readException.getLocalizedMessage() + fileName,readException);
            throw readException;
        } catch (ClassNotFoundException e) {
            NoSudokuClassException noSudokuClassException = new NoSudokuClassException("classNotFound",e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(noSudokuClassException.getLocalizedMessage(),noSudokuClassException);
            throw noSudokuClassException;
        }
        return board;
    }

    /**
     * Saves supplied <code>SudokuBoard</code> object to file.
     *
     *
     * @param obj <code>SudokuBoard</code> object to be saved
     */
    @Override
    public void write(SudokuBoard obj) throws WriteException {
        try {
            FileOutputStream fileStream = new FileOutputStream(fileName);
            outputStream = new ObjectOutputStream(fileStream);
            outputStream.writeObject(obj);
        } catch (IOException e) {
            WriteException writeException = new WriteException("writeError",e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(writeException.getLocalizedMessage() + fileName,writeException);
            throw writeException;
        }
    }

    /**
     * Responsible for successfully closing the file.
     * @throws CloseStreamException in case of failure
     *
     */
    @Override
    public void close() throws CloseStreamException {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            CloseStreamException closeStreamException = new CloseStreamException("streamClosingError",e);
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.error(closeStreamException.getLocalizedMessage() + fileName,closeStreamException);
            throw closeStreamException;
        }

    }
}
