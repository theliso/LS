package pt.isel.ls.Parser;

import pt.isel.ls.Commands.SubCommands.CloseIssue;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.SubCommands.Deletes.DeleteLabelFromIssue;
import pt.isel.ls.Commands.SubCommands.Gets.ByIds.GetIssueByProjectIDLabelName;
import pt.isel.ls.Commands.SubCommands.Gets.ByIds.GetIssueDetailedInfo;
import pt.isel.ls.Commands.SubCommands.Gets.ByIds.GetProjectDetailedInfo;
import pt.isel.ls.Commands.SubCommands.Gets.GetIssue;
import pt.isel.ls.Commands.SubCommands.Gets.GetProjectLabel;
import pt.isel.ls.Commands.SubCommands.Gets.GetProjects;
import pt.isel.ls.Commands.SubCommands.Gets.ByIds.GetCommentDetailedInfo;
import pt.isel.ls.Commands.SubCommands.OtherCommands.Style;
import pt.isel.ls.Commands.SubCommands.OpenIssue;
import pt.isel.ls.Commands.SubCommands.OtherCommands.*;
import pt.isel.ls.Commands.SubCommands.Posts.*;
import pt.isel.ls.Http.HttpServer;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Router {

    private static final Map<String, Supplier<Command>> commands = new HashMap<>();


    public static void initRouter() {
        addCommand();
    }

    private static void addCommand() {
        /* Add Project Management Commands */
        commands.put("POST /projects", CreateProject::new);
        commands.put("GET /projects", GetProjects::new);
        commands.put("GET /projects/{pid}", GetProjectDetailedInfo::new);
        /* Adds Issue Management Commands */
        commands.put("POST /projects/{pid}/issues", CreateIssue::new);
        commands.put("GET /projects/{pid}/issues", GetIssue::new);
        commands.put("GET /projects/{pid}/issues/{iid}", GetIssueDetailedInfo::new);
        commands.put("POST /projects/{pid}/issues/{iid}/comments", CreateComment::new);
        commands.put("POST /projects/{pid}/issues/{iid}/close", CloseIssue::new);
        commands.put("POST /projects/{pid}/issues/{iid}/open", OpenIssue::new);
        commands.put("GET /projects/{pid}/issues/{iid}/comments/{cid}", GetCommentDetailedInfo::new);
        /*Adds Headers possibilities*/
        commands.put("POST /projects/{pid}/labels", AssociateLabelToProject::new);
        commands.put("GET /projects/{pid}/labels", GetProjectLabel::new);
        commands.put("POST /projects/{pid}/issues/{iid}/labels", AssociateProjectLabelToIssue::new);
        commands.put("DELETE /projects/{pid}/issues/{iid}/labels/{label-name}", DeleteLabelFromIssue::new);
        commands.put("GET /projects/{pid}/labels/{label-name}", GetIssueByProjectIDLabelName::new);
        commands.put("GET /time", TimeCommand::new);
        commands.put("OPTION /", OptionCommand::new);
        commands.put("EXIT /", ExitCommand::new);
        commands.put("LISTEN /{port}", HttpServer::new);
        commands.put("GET /", MainPageHTML::new);
        commands.put("GET /projects/{pid}/search", GetIssuesPossibleQueries::new);
        /* Adds Style Command */
        commands.put("GET /style", Style::new);

    }


    public static Command getCommand(Request req) {
        Request mapCmd;
        for (String args : commands.keySet()) {
            mapCmd = new Request(args.split(" "));
            if (mapCmd.getMethod().contentEquals(req.getMethod())) {
                if (processPath(mapCmd.getPath(), req.getPath()))
                    return commands.get(args).get();
            }
        }
        return null;
    }

    private static boolean processPath(String mapCmdPath, String reqPath) {
        String[] mapSplit = mapCmdPath.substring(1).split("/");
        String[] reqSplit = reqPath.substring(1).split("/");
        if (mapSplit.length == reqSplit.length) {
            for (int i = 0; i < mapSplit.length; i++) {
                if (!mapSplit[i].contentEquals(reqSplit[i])
                        && (!mapSplit[i].matches("\\{\\w*\\}")
                        && !mapSplit[i].matches("\\{label-name\\}")))
                    return false;
            }
        } else
            return false;
        return true;
    }

    public static Map<String, Result> getPathDetails(String path) {
        String removeFirstBackSlash = path.substring(1);
        String[] split = removeFirstBackSlash.split("/");
        Map<String, Result> pathDetails = new HashMap<>();
        for (int i = 0; i < split.length; i++) {
            pathDetails.put(
                    split[i],
                    new Result(split[i], ((i + 1) < split.length) ? split[++i] : null)
            );
        }
        return pathDetails;
    }

    public static Map<String, Supplier<Command>> getCommands() {
        return commands;
    }

    public static Map<String, Result> getHeaderDetails(String req){
        Map<String, Result> headerDetails = new HashMap<>();
        if (req == null){
            headerDetails.put(
                    "accept",
                    new Result("accept", "text/html")
            );
            return headerDetails;
        }
        String[] split = req.split("\\|");
        final int KEY = 0;
        final int VALUE = 1;
        for (int i = 0; i < split.length; i++) {
            String[] pair = split[i].split(":");
            headerDetails.put(
                    pair[KEY],
                    new Result(pair[KEY], pair[VALUE])
            );
        }
        return headerDetails;
    }

    public static boolean isHTMLText(String header){
        Map<String, Result> headerDetails = getHeaderDetails(header);
        if (headerDetails.isEmpty()){
            return true;
        }
        if (headerDetails.containsKey("accept") &&
                headerDetails.get("accept").getString("accept").contentEquals("text/html")){
            return true;
        }
        if (headerDetails.containsKey("file-name")){
            return true;
        }
        return false;
    }

}
