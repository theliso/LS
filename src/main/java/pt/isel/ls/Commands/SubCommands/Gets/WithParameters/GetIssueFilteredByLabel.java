package pt.isel.ls.Commands.SubCommands.Gets.WithParameters;

import pt.isel.ls.Commands.SubCommands.Gets.SortType;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetIssueSortedBy;
import pt.isel.ls.dataAccess.IssueDto;

import java.util.List;
import java.util.Map;

public class GetIssueFilteredByLabel extends SortType {
    @Override
    protected List getProjectsAndIssuesSorted(
            Map<String, Result> params, Map<String, Result> ids, MySupplier connSupplier
    ) throws DataBaseInfoException {
        SQLGetIssueSortedBy sql = new SQLGetIssueSortedBy();
        String dir = null;
        if (params.containsKey("direction")) {
            dir = params.get("direction").getString("direction");
        }
        List<String> labels = null;
        if (params.containsKey("label")){
            labels = params.get("label").getList();
        }
        List<IssueDto> list = sql.getIssueFilteredByLabel(
                labels,
                dir == null ? "asc" : dir,
                connSupplier
        );
        return list;
    }
}
