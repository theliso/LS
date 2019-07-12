package pt.isel.ls.Commands.SubCommands.Posts;

import pt.isel.ls.data.SQLGetComment;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLCreateComment;
import pt.isel.ls.dataAccess.CommentDto;
import pt.isel.ls.htmlnew.HTML;
import pt.isel.ls.htmlnew.Node;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.sql.Date;
import java.util.Map;
import java.util.Set;

import static pt.isel.ls.Commands.CommandExtractor.getParametersExtracted;
import static pt.isel.ls.Parser.Router.getPathDetails;

public class CreateComment implements Command {
    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        Map<String, Result> paramExtracted = getParametersExtracted(params.getParameter());
        if (!validParameter(paramExtracted.keySet())) {
            return null;
        }
        Date currentDate = new Date(System.currentTimeMillis());
        CommentDto commentDto = new CommentDto(
                pathDetails.get("projects").getInt("projects"),
                currentDate,
                pathDetails.get("issues").getInt("issues"),
                paramExtracted.get("text").getString("text")
        );
        SQLCreateComment sql = new SQLCreateComment();
        int comment = sql.createComment(commentDto, connSupplier);
        SQLGetComment getComment = new SQLGetComment();
        CommentDto commentById = getComment.getCommentById(
                comment,
                pathDetails.get("issues").getInt("issues"),
                connSupplier
        );
        Tag html = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return html.html(
                        html.head(
                                html.href(
                                        html.h(
                                                1,
                                                new Text("Comment " + commentById.getIdComment())
                                        )
                                ).withAttributes(
                                        "href",
                                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                                "/issues/" + pathDetails.get("issues").getInt("issues")
                                )
                        ),
                        html.body(
                                html.paragraph(
                                        new Text("text: " + commentById.getText())
                                ),
                                html.paragraph(
                                        new Text("date: " + commentById.getDate())
                                ),
                                html.paragraph(
                                        new Text("issue associated: " + commentById.getIssue())
                                ),
                                html.paragraph(
                                        new Text("project associated: " + commentById.getIdProject())
                                )
                        )
                ).htmlFile();

            }

            @Override
            public String textPlain() {
                return "It was created a comment in a project issue";
            }
        };
    }

    private boolean validParameter(Set<String> param) throws InvalidParametersException {
        if (!param.contains("text")) {
            throw new InvalidParametersException("There is no text as parameter");
        }
        return true;
    }
}
