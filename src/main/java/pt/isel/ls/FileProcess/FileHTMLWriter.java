package pt.isel.ls.FileProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class FileHTMLWriter {

    public static void write(String htmlLayout, String fileName) {
        Logger logger = LoggerFactory.getLogger(FileHTMLWriter.class);
        if (fileName == null){
            System.out.println(htmlLayout);
            return;
        }
        logger.info("Writing to file {]", fileName);
        BufferedWriter writer;
        try {
            File file = new File(fileName);
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(htmlLayout);
            writer.close();
        } catch (Exception e) {
            logger.error("Couldn't write in the file specified. Error: {]", e.getMessage());
            //System.out.println("Couldn't write in the file specified");
        }
    }


}
