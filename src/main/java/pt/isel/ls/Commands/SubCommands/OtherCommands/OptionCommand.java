package pt.isel.ls.Commands.SubCommands.OtherCommands;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;

import java.util.HashMap;
import java.util.Map;

public class OptionCommand implements Command {
    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, String> map = new HashMap<>();
        map.put("EXIT /", "ends the application");
        map.put("GET /time", "presents the current time");
        map.put("OPTION /", "presents a list of available commands and their characteristics");
        map.put("GET /projects/{pid}/labels/{label-name}", "shows all issues with the label {label-name}");
        map.put("GET /projects/{pid}/issues/{iid}", "returns the detailed information for issue iid in project " +
                "pid, including its comments and the labels for the iid issue");
        map.put("GET /projects/{pid}/issues", "returns a list with all issues for project pid");
        map.put("DELETE /projects/{pid}/issues/{iid}/labels/{label-name}", "removes label-name from the iid issue");
        map.put("POST /projects/{pid}/issues/{iid}/labels", "adds a label to the issue iid, " +
                "given the following parameters");
        map.put("GET /projects/{pid}/labels", "shows the labels that can be used in the project identified by pid");
        map.put("POST /projects/{pid}/labels", "adds a new label usage to the project identified by pid");
        map.put("POST /projects/{pid}/issues/{iid}/close", "closes the issue iid in project pid");
        map.put("POST /projects/{pid}/issues/{iid}/open", "opens the issue iid in project pid");
        map.put("GET /projects/{pid}", "returns the detailed information for the project identified by pid");
        map.put("POST /projects/{pid}/issues/{iid}/comments", "creates a new comment, given the following parameters");
        map.put("POST /projects/{pid}/issues", "creates a new issue, given the following parameters");
        map.put("POST /projects", "creates a new project, given the following parameters");
        return new CommandResult() {
            @Override
            public String htmlView() {
                return textPlain();
            }

            @Override
            public String textPlain() {
                final String[] plain = {""};
                map.forEach((cmd, desc) -> {
                    plain[0] += cmd + " -> " + desc + "\n";
                });
                return plain[0];
            }
        };
    }
}
