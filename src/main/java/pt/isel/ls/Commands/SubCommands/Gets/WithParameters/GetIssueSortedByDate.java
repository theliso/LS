package pt.isel.ls.Commands.SubCommands.Gets.WithParameters;

import pt.isel.ls.Commands.SubCommands.Gets.SortType;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetIssueSortedBy;
import pt.isel.ls.dataAccess.IssueDto;

import java.util.List;
import java.util.Map;

public class GetIssueSortedByDate extends SortType {

    @Override
    protected List getProjectsAndIssuesSorted(
            Map<String, Result> params,
            Map<String, Result> ids,
            MySupplier connSupplier
    ) throws DataBaseInfoException {
        SQLGetIssueSortedBy sql = new SQLGetIssueSortedBy();
        Integer state = null;
        if (params.containsKey("state")) {
            if (params.get("state").getString("state").contentEquals("all")) {
                state = -1;
            } else {
                state = params.get("state").getString("state").contentEquals("open") ? OPEN : CLOSE;
            }
        }
        String dir = null;
        if (params.containsKey("direction")) {
            dir = params.get("direction").getString("direction");
        }
        List<String> labels = null;
        if (params.containsKey("label")){
            labels = params.get("label").getList();
        }
        List<IssueDto> list = sql.getIssueSortedByDate(
                ids.get("projects").getInt("projects"),
                params.get("sort").getString("sort"),
                state,
                dir == null ? "asc" : dir,
                labels,
                connSupplier
        );
        return list;
    }
}
