package pt.isel.ls.Commands;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;

public interface Command {

    CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException;

    public interface CommandResult{
        String htmlView();

        String textPlain();
    }

}
