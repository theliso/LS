package pt.isel.ls.Commands.SubCommands.OtherCommands;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.htmlnew.CSS;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import java.util.HashMap;
import java.util.Map;

public class MainPageHTML implements Command {


    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Tag html = new Tag();
        return new CommandResult() {
            @Override
            public String htmlView() {
                return html.html(
                        html.head(
                                html.h(
                                        1,
                                        new Text("LS project")
                                ),
                                html.style(
                                        new CSS(
                                                "buttonMain",
                                                setButtonCSS()
                                        )
                                )
                        ),
                        html.body(
                                html.anchor(
                                        html.button(
                                                new Text("Projects")
                                        ).withAttributes("class", "button")
                                ).withAttributes("href", "/projects")

                        )
                ).htmlFile();
            }

            private Map<String, String> setButtonCSS() {
                HashMap<String, String> map = new HashMap<>();
                map.put("background-color", "rgb(83, 85, 83)");
                map.put("border", "none");
                map.put("color", "white");
                map.put("padding", "15px 32px");
                map.put("text-align", "center");
                map.put("text-decoration", "none");
                map.put("display", "inline-block");
                map.put("font-size", "16px");
                map.put("margin", "4px 2px");
                map.put("cursor", "pointer");
                return map;
            }

            @Override
            public String textPlain() {
                return null;
            }
        };
    }
}
