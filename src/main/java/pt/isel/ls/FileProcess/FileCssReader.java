package pt.isel.ls.FileProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileCssReader {
    Logger logger = LoggerFactory.getLogger(FileCssReader.class);
    public String read()
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines( Paths.get("style.css"), StandardCharsets.UTF_8))
        {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        }
        catch (IOException e) {
            logger.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

}
