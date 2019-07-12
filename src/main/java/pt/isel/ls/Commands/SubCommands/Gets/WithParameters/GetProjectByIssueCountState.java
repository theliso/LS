package pt.isel.ls.Commands.SubCommands.Gets.WithParameters;

import pt.isel.ls.Commands.SubCommands.Gets.SortType;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetProjectsSortedBy;
import pt.isel.ls.dataAccess.ProjectDto;

import java.util.List;
import java.util.Map;

public class GetProjectByIssueCountState extends SortType {

    private final int CLOSE = 2;
    private final int OPEN = 1;

    @Override
    protected List getProjectsAndIssuesSorted(
            Map<String, Result> params, Map<String, Result> ids, MySupplier connSupplier
    ) throws DataBaseInfoException {
        SQLGetProjectsSortedBy sql = new SQLGetProjectsSortedBy();
        String dir = null;
        if (params.containsKey("direction")) {
            dir = params.get("direction").getString("direction");
        }
        int state = params.get("state").getString("state").contentEquals("open")
                ? OPEN : CLOSE;
        List<ProjectDto> res =
                sql.getProjectsSortedByIssueState(
                        state,
                        dir == null ? "asc" : dir,
                        connSupplier
                );
        return res;
    }
}
