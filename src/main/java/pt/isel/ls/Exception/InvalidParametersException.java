package pt.isel.ls.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidParametersException extends Exception {
    Logger logger = LoggerFactory.getLogger(InvalidParametersException.class);
    public InvalidParametersException(String message) {
        super(message);
        logger.error("Error: {}", message);
    }
}
