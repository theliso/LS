package pt.isel.ls.Commands.SubCommands.Gets.WithParameters;

import pt.isel.ls.Commands.SubCommands.Gets.SortType;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetIssueByProjectId;
import pt.isel.ls.dataAccess.IssueDto;

import java.util.List;
import java.util.Map;

public class GetIssueSortedByAll extends SortType {


    @Override
    protected List getProjectsAndIssuesSorted(
            Map<String, Result> params,
            Map<String, Result> ids,
            MySupplier connSupplier
    ) throws DataBaseInfoException {
        SQLGetIssueByProjectId sql = new SQLGetIssueByProjectId();
        List<String> label = null;
        if (params.containsKey("label")) {
            label = params.get("label").getList();
        }
        List<IssueDto> list = sql.getIssueByProjectId(
                ids.get("projects").getInt("projects"),
                label,
                connSupplier
        );
        return list;
    }
}
