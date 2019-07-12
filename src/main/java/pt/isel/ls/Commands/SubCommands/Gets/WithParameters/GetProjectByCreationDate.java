package pt.isel.ls.Commands.SubCommands.Gets.WithParameters;

import pt.isel.ls.Commands.SubCommands.Gets.SortType;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetProjectsSortedBy;
import pt.isel.ls.dataAccess.ProjectDto;

import java.util.List;
import java.util.Map;

public class GetProjectByCreationDate extends SortType {

    @Override
    protected List getProjectsAndIssuesSorted(
            Map<String, Result> params, Map<String, Result> ids, MySupplier connSupplier
    ) throws DataBaseInfoException {
        SQLGetProjectsSortedBy sql = new SQLGetProjectsSortedBy();
        String dir = null;
        if (params.containsKey("direction")) {
            dir = params.get("direction").getString("direction");
        }
        List<ProjectDto> res =
                sql.getProjectsSortedByDate(
                        dir == null ? "asc" : dir,
                        connSupplier
                );
        return res;
    }
}
