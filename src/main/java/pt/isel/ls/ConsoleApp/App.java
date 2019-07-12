package pt.isel.ls.ConsoleApp;

import pt.isel.ls.Exception.*;
import pt.isel.ls.Parser.CommandExecutor;

import static pt.isel.ls.Parser.Router.initRouter;

public class App {

    private final String[] arguments;
    private final CommandExecutor cmdExe = new CommandExecutor();

    public App(String[] arguments) {
        this.arguments = arguments;
    }

    public void run()
            throws InvalidParametersException, InvalidCommandException,
            ConnectionException, DataBaseInfoException, ExceededNumberOfArgumentsException {
        initRouter();
        cmdExe.run(arguments);
    }


}
