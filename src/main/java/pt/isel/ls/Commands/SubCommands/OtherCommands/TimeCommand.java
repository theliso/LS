package pt.isel.ls.Commands.SubCommands.OtherCommands;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeCommand implements Command {

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        LocalDateTime now = LocalDateTime.now();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return textPlain();
            }

            @Override
            public String textPlain() {
                return now.toString();
            }
        };
    }
}
