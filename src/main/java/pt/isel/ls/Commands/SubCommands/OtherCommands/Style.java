package pt.isel.ls.Commands.SubCommands.OtherCommands;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.FileProcess.FileCssReader;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;

public class Style implements Command {
    @Override
    public CommandResult execute(Request params, MySupplier connSupplier) throws DataBaseInfoException, InvalidParametersException {
        return new CommandResult() {
            @Override
            public String htmlView() {
                FileCssReader read = new FileCssReader();
                return read.read();
            }

            @Override
            public String textPlain() {
                return null;
            }
        };
    }
}
