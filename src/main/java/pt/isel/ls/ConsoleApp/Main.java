package pt.isel.ls.ConsoleApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);
        while(true) {
            try {
                App app;
                if (args.length != 0) {
                    app = new App(args);
                    app.run();
                    return;
                }
                Scanner sc = new Scanner(System.in);
                app = new App(sc.nextLine().split(" "));
                app.run();
            } catch (Exception e){
                logger.error("Error: {}", e.getMessage());
            }
        }
    }

}
