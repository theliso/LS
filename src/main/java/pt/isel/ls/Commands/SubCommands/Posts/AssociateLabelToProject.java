package pt.isel.ls.Commands.SubCommands.Posts;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLAssociateLabelToProject;
import pt.isel.ls.dataAccess.ProjectLabelDto;
import pt.isel.ls.htmlnew.*;

import java.util.Map;
import java.util.Set;

import static pt.isel.ls.Commands.CommandExtractor.getParametersExtracted;
import static pt.isel.ls.Parser.Router.getPathDetails;

public class AssociateLabelToProject implements Command {

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        Map<String, Result> paramExtracted = getParametersExtracted(params.getParameter());
        if (!validParameters(paramExtracted.keySet())) {
            return null;
        }
        ProjectLabelDto projLabel = new ProjectLabelDto(
                pathDetails.get("projects").getInt("projects"),
                paramExtracted.get("name").getString("name"),
                paramExtracted.get("color").getString("color")
        );
        SQLAssociateLabelToProject sql = new SQLAssociateLabelToProject();
        sql.associateLabelToProject(projLabel, connSupplier);
        Tag htmlFile = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        htmlFile.h(
                                                2,
                                                new Text("Labels for project with id" + projLabel.getProjectID())
                                        )
                                ).withAttributes(
                                        "href",
                                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                                "/labels"
                                )
                        ),
                        htmlFile.body(
                                htmlFile.paragraph(
                                        new Text("Label \"" + projLabel.getLabelName() + "\" was associated to project with id "+projLabel.getProjectID())
                                )
                        )
                ).htmlFile();
            }

            @Override
            public String textPlain() {
                return "It was associated a label to a project";
            }
        };
    }

    private boolean validParameters(Set<String> params) throws InvalidParametersException {
        if (!params.contains("name")) {
            throw new InvalidParametersException("There is no name in the label's project association");
        }
        if (!params.contains("color")) {
            throw new InvalidParametersException("There is no color in the label's project association");
        }
        return true;
    }
}
