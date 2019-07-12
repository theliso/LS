package pt.isel.ls.Commands.SubCommands.Gets.ByIds;

import pt.isel.ls.dataAccess.*;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.CommandExtractor;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetIssueById;
import pt.isel.ls.htmlnew.Node;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.util.List;
import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class GetIssueDetailedInfo extends CommandExtractor implements Command {

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        String issues = "issues";
        String projects = "projects";
        SQLGetIssueById sql = new SQLGetIssueById();
        final IssueDto issueDetailed = sql.getCommentsUsingIdIssue(
                pathDetails.get(issues).getInt(issues),
                pathDetails.get(projects).getInt(projects),
                connSupplier
        );
        return new CommandResult() {
            Tag htmlFile = new Tag();

            @Override
            public String htmlView() {
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        htmlFile.h(2,
                                                new Text("Project: id " + issueDetailed.getProjDto().getId()))
                                ).withAttributes("href", "/projects/" + issueDetailed.getProjDto().getId())
                        ),
                        htmlFile.body(
                                htmlFile.paragraph(
                                        new Text("Name: " + issueDetailed.getName())
                                ),
                                htmlFile.paragraph(
                                        new Text("Description: " + issueDetailed.getDescription())
                                ),
                                htmlFile.paragraph(
                                        new Text("Creation Date: " + issueDetailed.getCreationDate().toString())
                                ),
                                htmlFile.paragraph(
                                        new Text("Labels: "),
                                        getLabelsFromList(htmlFile,
                                                issueDetailed.getLabels(),
                                                issueDetailed.getProjDto().getId()
                                        )
                                ),
                                htmlFile.paragraph(
                                        new Text("Comments: ")
                                ),
                                htmlFile.paragraph(
                                        htmlFile.table(
                                                htmlFile.tableRow(
                                                        htmlFile.tableHead(new Text("Id")),
                                                        htmlFile.tableHead(new Text("Text")),
                                                        htmlFile.tableHead(new Text("Date"))
                                                ),
                                                getCommentsFromList(htmlFile,
                                                        issueDetailed.getComments()
                                                )
                                        )
                                ),
                                setFormToOpenOrCloseIssue(),
                                setFormToAddLabel(),
                                setForm()

                        )
                ).htmlFile();
            }

            private Node setFormToOpenOrCloseIssue() {
                int status = issueDetailed.getStatusDto().getId();
                String statusStr;
                if (status == 1) statusStr = "close";
                else statusStr = "open";
                return htmlFile.form(
                        htmlFile.button(
                                new Text(statusStr + " issue")
                        ).withAttributes("type", "submit")
                ).withAttributes(
                        "action",
                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                "/issues/" + pathDetails.get("issues").getInt("issues") + "/" +
                                statusStr
                ).withAttributes("method", "POST");
            }

            private Node setFormToAddLabel() {
                return htmlFile.form(
                        new Text("Add label to this issue: "),
                        htmlFile.input()
                                .withAttributes("type", "text")
                                .withAttributes("name", "name"),
                        htmlFile.button(
                                new Text(
                                        "Submit"
                                )
                        ).withAttributes("type", "submit")
                ).withAttributes(
                        "action",
                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                "/issues/" + issueDetailed.getId() +
                                "/labels"
                ).withAttributes("method", "POST");
            }

            private Node setForm() {
                return htmlFile.form(
                        new Text("Comment text:"),
                        htmlFile.input()
                                .withAttributes("type", "text")
                                .withAttributes("name", "text"),
                        htmlFile.button(
                                new Text(
                                        "Submit"
                                )
                        ).withAttributes("type", "submit")
                ).withAttributes(
                        "action",
                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                "/issues/" + pathDetails.get("issues").getInt("issues") +
                                "/comments"
                ).withAttributes("method", "POST");
            }

            @Override
            public String textPlain() {
                String plain = "Project [id: " + issueDetailed.getProjDto().getId() + "]\n";

                final String[] label = {""};
                issueDetailed.getLabels().forEach(lab -> label[0] += lab.getName() + ",");
                String labels = label[0].substring(0, label[0].length() - 1);

                plain += "Issue [id: " + issueDetailed.getId() + "]" + "\n" +
                        "name: " + issueDetailed.getName() + "\n" +
                        "description: " + issueDetailed.getDescription() + "\n" +
                        "creation date: " + issueDetailed.getCreationDate() + "\n" +
                        "labels:" + labels;

                final String[] comments = {""};
                issueDetailed.getComments().forEach(
                        comment -> {
                            if (comment != null) {
                                comments[0] += "date: " + comment.getDate() + "\n" +
                                        "text: " + comment.getText() + "\n";
                            }
                        }
                );
                if (!comments[0].contentEquals("")) {
                    plain += "Comments\n" + comments[0];
                }
                return plain;
            }
        };
    }

    private Node getLabelsFromList(Tag htmlFile, List<LabelDto> list, int projectId) {
        Node[] listNode = new Node[list.size()];
        for (int i = 0; i < list.size(); i++) {
            LabelDto label = list.get(i);
            if (i != list.size() - 1)
                listNode[i] = htmlFile.href(
                        new Text(label.getName() + ", ")
                ).withAttributes("href", "/projects/" + projectId + "/labels/" + label.getName());
            else listNode[i] = htmlFile.href(
                    new Text(label.getName())
            ).withAttributes("href", "/projects/" + projectId + "/labels/" + label.getName());
        }
        return htmlFile.div(listNode);
    }

    private Node getCommentsFromList(Tag htmlFile, List<CommentDto> list) {
        Node[] listNode = new Node[list.size()];
        for (int i = 0; i < list.size(); i++) {
            CommentDto comment = list.get(i);
            listNode[i] = htmlFile.tableRow(
                    htmlFile.tableData(
                            htmlFile.href(
                                    new Text(String.valueOf(comment.getIdComment()))
                            ).withAttributes("href", "/projects/" +
                                    comment.getIdProject() +
                                    "/issues/" +
                                    comment.getIssue() +
                                    "/comments/" +
                                    comment.getIdComment())
                    ),
                    htmlFile.tableData(
                            new Text(comment.getText())
                    ),
                    htmlFile.tableData(
                            new Text(comment.getDate().toString())
                    )
            );
        }
        return htmlFile.div(listNode);
    }

}
