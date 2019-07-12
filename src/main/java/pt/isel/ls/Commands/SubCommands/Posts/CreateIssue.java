package pt.isel.ls.Commands.SubCommands.Posts;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLCreateIssue;
import pt.isel.ls.dataAccess.IssueDto;
import pt.isel.ls.dataAccess.ProjectDto;
import pt.isel.ls.dataAccess.StatusDto;
import pt.isel.ls.htmlnew.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static pt.isel.ls.Commands.CommandExtractor.*;
import static pt.isel.ls.Parser.Router.*;

public class CreateIssue implements Command {

    private final int OPEN = 1;
    private static final List<String> PARAMS = new ArrayList<>();
    private final int EMPTY = 0;

    static {
        PARAMS.add("name");
        PARAMS.add("description");
        PARAMS.add("label");
    }

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, Result> paramExtracted = getParametersExtracted(params.getParameter());
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        if (paramExtracted.size() == EMPTY){
            throw new DataBaseInfoException("should give the name, description and label as parameters");
        }
        if (!validParameters(paramExtracted.keySet())) {
            return null;
        }
        Date currentDate = new Date(System.currentTimeMillis());
        String description = null;
        if (paramExtracted.containsKey("description"))
            description = paramExtracted.get("description").getString("description");
        IssueDto issueDto = new IssueDto(
                new ProjectDto(pathDetails.get("projects").getInt("projects")),
                new StatusDto(OPEN),
                paramExtracted.get("name").getString("name"),
                description,
                currentDate,
                currentDate
        );
        SQLCreateIssue sql = new SQLCreateIssue();
        int issueId = sql.createIssue(issueDto, connSupplier);
        Tag html = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return html.html(
                        html.head(
                                html.href(
                                        html.h(
                                                1,
                                                new Text("Issue " + issueId)
                                        )
                                ).withAttributes(
                                        "href",
                                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                                "/issues/" + issueId
                                )
                        ),
                        html.body(
                                html.paragraph(
                                        new Text("name: " + issueDto.getName())
                                ),
                                html.paragraph(
                                        new Text("description: " + issueDto.getDescription())
                                ),
                                html.paragraph(
                                        new Text("creation date: " + issueDto.getCreationDate().toString())
                                ),
                                html.paragraph(
                                        new Text("nr Comments: " + issueDto.getNrComments())
                                )
                        )
                ).htmlFile();
            }


            @Override
            public String textPlain() {
                return "It was created an issue in a project (issue id = " + issueId + ")";
            }
        };
    }

    private boolean validParameters(Set<String> param) throws InvalidParametersException {
        Stream<String> validParam = param.stream().filter(elem -> !PARAMS.contains(elem));
        if (validParam.toArray().length > EMPTY){
            throw new InvalidParametersException("the parameters were wrongly typped");
        }
        return true;
    }
}
