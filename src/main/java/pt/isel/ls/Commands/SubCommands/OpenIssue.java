package pt.isel.ls.Commands.SubCommands;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLUpdateIssue;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class OpenIssue implements Command {

    private final int OPEN = 1;

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier) throws DataBaseInfoException {
        SQLUpdateIssue sql = new SQLUpdateIssue();
        String path = params.getPath();
        Map<String, Result> paramExtracted = getPathDetails(path);
        sql.updateIssue(paramExtracted.get("projects").getInt("projects"),
                paramExtracted.get("issues").getInt("issues"),
                OPEN,
                connSupplier
        );
        Tag htmlFile = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        htmlFile.h(
                                                2,
                                                new Text("Issue " + paramExtracted.get("issues").getInt("issues")
                                                )
                                        )
                                ).withAttributes(
                                        "href",
                                        "/projects/" + paramExtracted.get("projects").getInt("projects") +
                                                "/issues/" + paramExtracted.get("issues").getInt("issues")
                                )
                        ),
                        htmlFile.body(
                                htmlFile.paragraph(
                                        new Text("Issue with id " + paramExtracted.get("issues").getInt("issues") + " was opened!")
                                )
                        )
                ).htmlFile();
            }

            @Override
            public String textPlain() {
                return "Issue Updated!";
            }
        };
    }
}
