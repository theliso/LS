package pt.isel.ls.Parser;

import static pt.isel.ls.Parser.CommandState.INVALID;
import static pt.isel.ls.Parser.CommandState.VALID;

public class Request {

    private String method;
    private String path;
    private String parameter;
    private String header;
    private CommandState state;

    private static final int METHOD = 0;
    private static final int PATH = 1;
    private static final int HEADER = 2;
    private static final int PARAMETER = 3;
    private static final int ALL_COMPONENTS = 4;
    private static final int WITHOUT_OPTIONAL_COMPONENT = 3;

    public Request(String[] commandArgs) {
        fillComponents(commandArgs);
    }

    private void fillComponents(String[] commandArgs) {
        this.method = commandArgs[METHOD];
        this.path = commandArgs[PATH];
        if (commandArgs.length == ALL_COMPONENTS) {
            if (fillHeaderAndParamenterComponents(commandArgs)) {
                state = VALID;
            } else
                state = INVALID;
            return;
        }
        if (commandArgs.length == WITHOUT_OPTIONAL_COMPONENT) {
            if (fillHeaderAndParamenterComponents(commandArgs)) {
                state = VALID;
            } else
                state = INVALID;
        }
    }

    private boolean fillHeaderAndParamenterComponents(String[] commandArgs) {
        for (int i = PATH + 1; i < commandArgs.length; i++) {
            if (commandArgs[i].contains(":") && commandArgs[i].contains("=")) {
                return false;
            }
            if (commandArgs[i].contains(":")) {
                this.header = commandArgs[HEADER];
                if (PARAMETER < commandArgs.length)
                    this.parameter = commandArgs[PARAMETER];
                return true;
            }
            if (commandArgs[i].contains("=")) {
                this.parameter = commandArgs[i];
                return true;
            }
        }
        return false;
    }

    public String getMethod() {
        return method;
    }

    public String getParameter() {
        return parameter;
    }

    public String getPath() {
        return path;
    }

    public String getHeader() {
        return header;
    }

    public CommandState getState() {
        return state;
    }
}
