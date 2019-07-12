package pt.isel.ls.Commands.SubCommands.Gets.ByIds;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.CommandExtractor;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetProjectById;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.dataAccess.ProjectDto;
import pt.isel.ls.htmlnew.Node;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class GetProjectDetailedInfo extends CommandExtractor implements Command {

    private Tag html = new Tag();

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        String projects = "projects";
        SQLGetProjectById sql = new SQLGetProjectById();
        ProjectDto project = sql.getProjectAndLabelsById(
                pathDetails.get(projects).getInt(projects),
                connSupplier
        );
        return new CommandResult() {
            @Override
            public String htmlView() {

                return html.html(
                        html.head(
                                html.h(1,
                                        html.href(new Text("Projects"))
                                                .withAttributes("href", "/projects")
                                )
                        ),
                        html.body(
                                html.paragraph(
                                        new Text("Id:"),
                                        html.href(new Text("" + project.getId()))
                                                .withAttributes("href", "/projects/" + project.getId() + "/search")
                                ),
                                html.paragraph(
                                        new Text("Name: " + project.getName())
                                ),
                                html.paragraph(
                                        new Text("Description: " + project.getDescription())
                                ), html.paragraph(
                                        new Text("Labels: "), insertLabels(project)
                                ),
                                html.paragraph(
                                        setFormToAddLabel()
                                )
                        )

                ).htmlFile();
            }

            private Node setFormToAddLabel() {
                return html.form(
                        new Text("Add label to this project: name ->"),
                        html.input()
                                .withAttributes("type", "text")
                                .withAttributes("name", "name"),
                        new Text("color ->"),
                        html.input()
                                .withAttributes("type", "text")
                                .withAttributes("name", "color"),
                        html.button(
                                new Text(
                                        "Submit"
                                )
                        ).withAttributes("type", "submit")
                ).withAttributes(
                        "action",
                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                "/labels"
                ).withAttributes("method", "POST");
            }

            @Override
            public String textPlain() {
                return "id: " + project.getId() + "\n" +
                        "name: " + project.getName() + "\n" +
                        "description: " + project.getDescription() + "\n" +
                        "creation date: " + project.getCreationDate();
            }
        };
    }

    private Node insertLabels(ProjectDto project) {
        int size = 0;
        if (project.getLabels() != null) {
            size = project.getLabels().size();
            Node[] nodes = new Node[size];
            int i = 0;
            for (LabelDto label : project.getLabels()) {
                nodes[i] = html.href(new Text(label.getName()))
                        .withAttributes("href", "/projects/" + project.getId() + "/labels/" + label.getName());
                ++i;
            }
            return html.div(nodes);
        }
        return new Text("No labels associated");
    }
}
