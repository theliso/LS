package pt.isel.ls.Commands.SubCommands.Gets.ByIds;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetIssuesByLabel;
import pt.isel.ls.dataAccess.IssueDto;
import pt.isel.ls.htmlnew.*;

import java.util.List;
import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class GetIssueByProjectIDLabelName implements Command {
    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        SQLGetIssuesByLabel sql = new SQLGetIssuesByLabel();
        List<IssueDto> list = sql.getIssuesByLabel(
                pathDetails.get("projects").getInt("projects"),
                pathDetails.get("labels").getString("labels"),
                connSupplier
        );
        Tag htmlFile = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                if (list.isEmpty())
                    return setNullIssuePage();
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        htmlFile.h(2,
                                                new Text("Project id: " + list.get(0).getProjDto().getId()))
                                ).withAttributes("href",
                                        "/projects/" + list.get(0).getProjDto().getId())
                        ),
                        htmlFile.body(
                                htmlFile.table(
                                        htmlFile.tableRow(
                                                htmlFile.tableHead(new Text("id")),
                                                htmlFile.tableHead(new Text("name")),
                                                htmlFile.tableHead(new Text("description")),
                                                htmlFile.tableHead(new Text("creation date")),
                                                htmlFile.tableHead(new Text("updated date")),
                                                htmlFile.tableHead(new Text("status")),
                                                htmlFile.tableHead(new Text("issue"))
                                        ),
                                        getTrsFromList(htmlFile, list)
                                ).withAttributes("class", "limiter")
                        )
                ).htmlFile();
            }

            @Override
            public String textPlain() {
                final String[] plain = {""};
                list.forEach(
                        issue -> {
                            String status = issue.getStatusDto().getId() == 1 ? "close" : "open";
                            plain[0] += "id: " + issue.getId() + "\n" +
                                    "name: " + issue.getName() + "\n" +
                                    "description: " + issue.getDescription() + "\n" +
                                    "creation date: " + issue.getCreationDate() + "\n" +
                                    "updated date: " + issue.getUpdateDate() + "\n" +
                                    "status: " + status + "\n";
                        }
                );
                return plain[0];
            }

            private String setNullIssuePage() {
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        htmlFile.h(2,
                                                new Text("Projects")
                                        )
                                ).withAttributes("href", "/projects")
                        ),
                        htmlFile.body(
                                htmlFile.paragraph(
                                        new Text("there is no comments associated with this issue")
                                ),
                                htmlFile.br(),
                                htmlFile.form(
                                        htmlFile.button(
                                                new Text("add issues to projects")
                                        )
                                ).withAttributes("action", "/projects/" +
                                        pathDetails.get("projects").getInt("projects") +
                                        "/issues"
                                )
                        )
                ).htmlFile();
            }
        };
    }

    private Node getTrsFromList(Tag htmlFile, List<IssueDto> list) {
        Node[] listNode = new Node[list.size()];
        for (int i = 0; i < list.size(); i++) {
            IssueDto issue = list.get(i);
            listNode[i] = htmlFile.tableRow(
                    htmlFile.tableData(
                            htmlFile.href(
                                    new Text(String.valueOf(issue.getId()))
                            ).withAttributes("href", "/projects/" + issue.getProjDto().getId() + "/issues/" + issue.getId())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getName())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getDescription())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getCreationDate().toString())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getUpdateDate().toString())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getStatusDto().getDescription())
                    )
            );
        }
        return htmlFile.div(listNode);
    }
}
