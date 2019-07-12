package pt.isel.ls.Commands.SubCommands.Gets.ByIds;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetComment;
import pt.isel.ls.dataAccess.CommentDto;
import pt.isel.ls.htmlnew.Node;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class GetCommentDetailedInfo implements Command {

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        String comments = "comments";
        String issues = "issues";
        SQLGetComment sql = new SQLGetComment();
        CommentDto commentDetailed = sql.getCommentById(
                pathDetails.get(comments).getInt(comments),
                pathDetails.get(issues).getInt(issues),
                connSupplier
        );
        Tag html = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                if (commentDetailed == null) {
                    return setNullHTMLCommentPage();
                }
                return html.html(
                        html.head(
                                html.href(
                                        html.h(2,
                                                new Text(
                                                        "Issue: " + pathDetails.get("issues").getInt("issues")
                                                )
                                        )
                                ).withAttributes(
                                        "href",
                                        "/projects/"
                                                + pathDetails.get("projects").getInt("projects") +
                                                "/issues/" +
                                                pathDetails.get("issues").getInt("issues")
                                )

                        ),
                        html.body(
                                html.paragraph(
                                        new Text("comment: " + commentDetailed.getText())
                                ),
                                html.paragraph(
                                        new Text("date time: " + commentDetailed.getDate().toString())
                                ),
                                html.form(
                                        setPreviousButton(),
                                        setNextButton()
                                ),
                                html.br()
                        )
                ).htmlFile();
            }

            private Node setNextButton() {
                if (sql.hasCommentAdded(
                        1, commentDetailed.getIdComment(), commentDetailed.getIssue(), connSupplier
                )) {
                    String hRef = "/projects/" + commentDetailed.getIdProject() +
                            "/issues/" + commentDetailed.getIssue();
                    return html.button(
                            new Text(
                                    "next"
                            )
                    ).withAttributes(
                            "formaction",
                            hRef + "/comments/" + (commentDetailed.getIdComment() + 1)
                    );
                }
                return html.button(
                        new Text(
                                "next"
                        )
                ).withAttributes("disabled", "disabled");
            }

            private Node setPreviousButton() {
                if (sql.hasCommentAdded(
                        -1, commentDetailed.getIdComment(), commentDetailed.getIssue(), connSupplier
                )) {
                    String hRef = "/projects/" + commentDetailed.getIdProject() +
                            "/issues/" + commentDetailed.getIssue();
                    return html.button(
                            new Text(
                                    "previous"
                            )
                    ).withAttributes(
                            "formaction",
                            hRef + "/comments/" + (commentDetailed.getIdComment() - 1)
                    );
                }
                return html.button(
                        new Text(
                                "previous"
                        )
                ).withAttributes("disabled", "disabled");
            }

            private String setNullHTMLCommentPage() {
                return html.html(
                        html.head(
                                html.href(
                                        new Text(
                                                "/projects/"
                                                        + pathDetails.get("projects").getInt("projects") +
                                                        "/issues"
                                        ),
                                        html.h(1,
                                                new Text(
                                                        "Project "
                                                                + pathDetails.get("projects").getInt("projects")
                                                )
                                        )
                                )
                        ),
                        html.body(
                                html.paragraph(
                                        new Text("there is no comments associated with this issue")
                                )
                        )
                ).htmlFile();
            }

            @Override
            public String textPlain() {
                return "text: " + commentDetailed.getText() + "\n" +
                        "date time: " + commentDetailed.getDate();
            }
        };
    }
}
