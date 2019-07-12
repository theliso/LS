package pt.isel.ls.Commands.SubCommands.Gets;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetProjectLabels;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.htmlnew.*;

import java.util.List;
import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class GetProjectLabel implements Command {
    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        SQLGetProjectLabels sql = new SQLGetProjectLabels();
        List<LabelDto> labels = sql.getProjectLabel(
                pathDetails.get("projects").getInt("projects"),
                connSupplier
        );
        Tag htmlFile = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        htmlFile.h(2,
                                                new Text("Project id: "+
                                                        pathDetails.get("projects").getInt("projects"))
                                        )
                                ).withAttributes("href",
                                        "/projects/"+pathDetails.get("projects").getInt("projects"))
                        ),
                        htmlFile.body(
                                htmlFile.paragraph(
                                        new Text("Labels: ")
                                ),
                                getLabelsFromList(htmlFile,
                                        labels,
                                        pathDetails.get("projects").getInt("projects")
                                )
                        )
                ).htmlFile();
            }

            @Override
            public String textPlain() {
                final String[] plain = {
                        "Project' Labels [id: " + pathDetails.get("projects").getString("projects") + "]\n"
                };
                labels.forEach(
                        label -> {
                            plain[0] += "name: " + label.getName() + "\n";
                        }
                );
                return plain[0];
            }
        };
    }

    private Node getLabelsFromList(Tag htmlFile, List<LabelDto> list, int projectId) {
        Node[] listNode = new Node[list.size()];
        for(int i = 0; i<list.size(); i++) {
            LabelDto label = list.get(i);
            listNode[i] = htmlFile.href(
                    new Text(label.getName())
            ).withAttributes("href", "/projects/"+projectId+"/labels/"+label.getName());
        }
        return htmlFile.div(listNode);
    }
}
