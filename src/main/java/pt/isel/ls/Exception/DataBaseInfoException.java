package pt.isel.ls.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class DataBaseInfoException extends SQLException {
    Logger logger = LoggerFactory.getLogger(DataBaseInfoException.class);
    public DataBaseInfoException(String message) {
        super(message);
        logger.error("Error: {}", message);
    }
}
