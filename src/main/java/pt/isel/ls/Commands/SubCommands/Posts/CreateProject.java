package pt.isel.ls.Commands.SubCommands.Posts;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLCreateProject;
import pt.isel.ls.data.SQLGetProjectById;
import pt.isel.ls.data.SQLGetProjects;
import pt.isel.ls.dataAccess.ProjectDto;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.util.Map;
import java.util.Set;

import static pt.isel.ls.Commands.CommandExtractor.getParametersExtracted;

public class CreateProject implements Command {

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, Result> paramDetails = getParametersExtracted(params.getParameter());
        if (!validParameters(paramDetails.keySet())) {
            return null;
        }
        String description = null;
        if (paramDetails.containsKey("description"))
            description = paramDetails.get("description").getString("description");
        ProjectDto proj = new ProjectDto(
                paramDetails.get("name").getString("name"),
                description
        );
        SQLCreateProject sql = new SQLCreateProject();
        int projectId = sql.createProject(proj, connSupplier);
        SQLGetProjectById project = new SQLGetProjectById();
        ProjectDto projectById = project.getProjectById(projectId, connSupplier);
        Tag html = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return html.html(
                        html.head(
                                html.href(
                                        html.h(
                                                1,
                                                new Text("Projects")
                                        )
                                ).withAttributes("href", "/projects"),
                                html.href(
                                        html.h(
                                                3,
                                                new Text("Project: " + projectId)
                                        )
                                ).withAttributes("href", "/projects/" + projectId)
                        ),
                        html.body(

                                html.paragraph(
                                        new Text("name: " + projectById.getName())
                                ),
                                html.paragraph(
                                        new Text("description: " + (projectById.getDescription() != null ?
                                                projectById.getDescription() : "no description inserted"))
                                )
                        )
                ).htmlFile();
            }

            @Override
            public String textPlain() {
                return "It was started a project (id = " + projectId + ")";
            }
        };

    }

    private boolean validParameters(Set<String> params)
            throws InvalidParametersException {
        if (!params.contains("name")) {
            throw new InvalidParametersException("The parameter name does not exists");
        }
        if (params.size() == 2) {
            if (!params.contains("description")) {
                throw new InvalidParametersException("The parameter description does not exists");
            }
        }
        return true;
    }


}
