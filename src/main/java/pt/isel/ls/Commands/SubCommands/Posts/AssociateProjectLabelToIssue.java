package pt.isel.ls.Commands.SubCommands.Posts;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLCreateIssueLabel;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.dataAccess.ProjectLabelIssueDto;
import pt.isel.ls.htmlnew.*;

import java.util.List;
import java.util.Map;

import static pt.isel.ls.Commands.CommandExtractor.getParametersExtracted;
import static pt.isel.ls.Parser.Router.getPathDetails;

public class AssociateProjectLabelToIssue implements Command {


    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        Map<String, Result> paramExtracted = getParametersExtracted(params.getParameter());
        for (String paramName : paramExtracted.keySet()) {
            if (!validParameter(paramName)) {
                return null;
            }
        }
        ProjectLabelIssueDto projLabIssueDto = new ProjectLabelIssueDto(
                pathDetails.get("projects").getInt("projects"),
                pathDetails.get("issues").getInt("issues"),
                paramExtracted.get("name").getList()
        );
        SQLCreateIssueLabel sql = new SQLCreateIssueLabel();
        sql.createIssueLabel(projLabIssueDto, connSupplier);
        Tag htmlFile = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        htmlFile.h(
                                                2,
                                                new Text("Label " + projLabIssueDto.getIssueID())
                                        )
                                ).withAttributes(
                                        "href",
                                        "/projects/" +
                                                pathDetails.get("projects").getInt("projects") +
                                                "/issues/"+projLabIssueDto.getIssueID()
                                )
                        ),
                        htmlFile.body(
                                new Text("Labels " + getLabelsFromList(htmlFile,
                                        paramExtracted.get("name").getList(),
                                        projLabIssueDto.getProjectID()) +
                                        " were associated to issue with id " +
                                        projLabIssueDto.getIssueID())
                        )
                ).htmlFile();
            }

            @Override
            public String textPlain() {
                return "It was associated a label's project to an issue";
            }
        };
    }

    private String getLabelsFromList(Tag htmlFile, List<String> list, int projectId) {
        Node[] listNode = new Node[list.size()];
        for(int i = 0; i<list.size(); i++) {
            String label = list.get(i);
            listNode[i] = htmlFile.href(
                    new Text("\"" + label + "\"")
            ).withAttributes("href", "/projects/"+projectId+"/labels/"+label);
        }
        return htmlFile.div(listNode).htmlFile();
    }

    private boolean validParameter(String param) throws InvalidParametersException {
        if (!param.contentEquals("name")) {
            throw new InvalidParametersException("There is no name in label issue");
        }
        return true;
    }


}
