package pt.isel.ls.Commands.SubCommands.Gets;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Result;

import java.util.List;
import java.util.Map;

public abstract class SortType {


    protected final int OPEN = 1;
    protected final int CLOSE = 2;

    protected abstract List getProjectsAndIssuesSorted(
            Map<String, Result> params, Map<String, Result> ids, MySupplier connSupplier
    ) throws DataBaseInfoException;
}
