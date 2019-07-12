package pt.isel.ls.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidCommandException extends Exception {
    Logger logger = LoggerFactory.getLogger(InvalidCommandException.class);
    public InvalidCommandException(String msg) {
        super(msg);
        logger.error("Error: {}", msg);
    }
}
