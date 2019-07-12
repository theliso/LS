package pt.isel.ls.Commands.SubCommands.Deletes;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLDeleteLabelFromIssue;
import pt.isel.ls.dataAccess.ProjectLabelIssueDto;

import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class DeleteLabelFromIssue implements Command {

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        ProjectLabelIssueDto projectLabelIssueDto = new ProjectLabelIssueDto(
                pathDetails.get("projects").getInt("projects"),
                pathDetails.get("issues").getInt("issues"),
                pathDetails.get("labels").getString("labels")
        );
        SQLDeleteLabelFromIssue sql = new SQLDeleteLabelFromIssue();
        sql.DeleteLabelFromIssue(projectLabelIssueDto, connSupplier);
        return null;
    }
}
