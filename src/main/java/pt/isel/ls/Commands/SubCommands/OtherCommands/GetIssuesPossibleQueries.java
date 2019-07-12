package pt.isel.ls.Commands.SubCommands.OtherCommands;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetProjectLabels;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.htmlnew.HTML;
import pt.isel.ls.htmlnew.Node;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.util.*;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class GetIssuesPossibleQueries implements Command {

    private static Set<String> possibleState = new HashSet<>();
    private static Set<String> possibleDirection = new HashSet<>();
    private static Set<String> possibleSort = new HashSet<>();
    private static Set<String> possibleLabels = new HashSet<>();

    private static void addParameters(List<LabelDto> labels) {
        /* add possible states */
        possibleState.add("all");
        possibleState.add("open");
        possibleState.add("closed");
        /* add possible sort */
        possibleSort.add("created");
        possibleSort.add("updated");
        possibleSort.add("comments");
        /* add possible direction */
        possibleDirection.add("asc");
        possibleDirection.add("desc");
        /* add possible labels */
        for (LabelDto label : labels) possibleLabels.add(label.getName());
    }

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        SQLGetProjectLabels sql = new SQLGetProjectLabels();
        List<LabelDto> projectLabels = sql.getProjectLabel(
                pathDetails.get("projects").getInt("projects"),
                connSupplier
        );
        addParameters(projectLabels);
        Tag html = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                String url = "/projects/%s";
                url = String.format(url, String.valueOf(pathDetails.get("projects").getInt("projects")));
                return html.html(
                        html.head(
                                html.h(
                                        1,
                                        new Text("Possible Queries")
                                )
                        ),
                        html.body(
                                html.form(
                                        html.select(
                                                setLabelOptions()
                                        ).withAttributes("name", "label"),
                                        html.select(
                                                setDirectionOptions()
                                        ).withAttributes("name", "direction"),
                                        html.select(
                                                setStateOptions()
                                        ).withAttributes("name", "state"),
                                        html.select(
                                                setSortOptions()
                                        ).withAttributes("name", "sort"),
                                        new HTML("br"),
                                        html.button(
                                                new Text("Submit")
                                        ).withAttributes("type", "submit")
                                ).withAttributes("method", "get")
                                        .withAttributes(
                                                "action",
                                                "/projects/" +
                                                        pathDetails.get("projects").getString("projects") +
                                                        "/issues"
                                        )
                        )
                ).htmlFile();
            }

            private Node[] setSortOptions() {
                Node[] array = new Node[possibleSort.size()];
                final int[] i = {0};
                possibleSort.forEach(sort -> {
                    array[i[0]++] = html.option(
                            new Text(
                                    sort
                            )
                    ).withAttributes("value", sort);
                });
                return array;
            }

            private Node[] setStateOptions() {
                Node[] array = new Node[possibleState.size()];
                final int[] i = {0};
                possibleState.forEach(state -> {
                    array[i[0]++] = html.option(
                            new Text(
                                    state
                            )
                    ).withAttributes("value", state);
                });
                return array;
            }

            private Node[] setDirectionOptions() {
                Node[] array = new Node[possibleDirection.size()];
                final int[] i = {0};
                possibleDirection.forEach(dir -> {
                    array[i[0]++] = html.option(
                            new Text(
                                    dir
                            )
                    ).withAttributes("value", dir);
                });
                return array;
            }

            private Node[] setLabelOptions() {
                Node[] array = new Node[projectLabels.size() + 1];
                array[0] = html.option(
                        new Text(
                                ""
                        )
                ).withAttributes("value", "");
                final int[] i = {1};
                projectLabels.forEach(label -> {
                    array[i[0]++] = html.option(
                            new Text(
                                    label.getName()
                            )
                    ).withAttributes("value", label.getName());
                });
                return array;
            }

            @Override
            public String textPlain() {
                return null;
            }
        };

    }
}


