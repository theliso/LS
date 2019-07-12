package pt.isel.ls.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExceededNumberOfArgumentsException extends Exception {
    Logger logger = LoggerFactory.getLogger(ExceededNumberOfArgumentsException.class);
    public ExceededNumberOfArgumentsException(String message) {
        super(message);
        logger.error("Error: {}", message);
    }
}
