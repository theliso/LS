package pt.isel.ls.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionException extends Exception {
    Logger logger = LoggerFactory.getLogger(ConnectionException.class);
    public ConnectionException(String message) {
        super(message);
        logger.error("Error: {}", message);
    }
}
