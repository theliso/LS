package pt.isel.ls.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoCommandGivenException extends RuntimeException {
    Logger logger = LoggerFactory.getLogger(NoCommandGivenException.class);
    public NoCommandGivenException(String s) {
        super(s);
        logger.error("Error: {}", s);
    }
}
