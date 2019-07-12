package pt.isel.ls.Commands.SubCommands.Gets;

import pt.isel.ls.HtmlPages.HtmlProjectsTablePage;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.CommandExtractor;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetProjects;
import pt.isel.ls.dataAccess.ProjectDto;
import java.util.*;
import java.util.stream.Stream;

import static pt.isel.ls.data.SQLGet.getSortParameter;

public class GetProjects extends CommandExtractor implements Command {

    private final int EMPTY = 0;

    private static final ArrayList<String> params = new ArrayList<>();

    static {
        params.add("sort");
        params.add("direction");
    }

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> paramsExtractor = getParametersExtracted(params.getParameter());
        List<ProjectDto> projects = null;
        if (paramsExtractor == null || paramsExtractor.isEmpty()) {
            SQLGetProjects sql = new SQLGetProjects();
            projects = sql.getProjects(connSupplier);
        } else {
            if (!validParameters(paramsExtractor.keySet())) {
                return null;
            }
            SortType type;
            if (paramsExtractor.containsKey("sort")) {
                type = getSortParameter(paramsExtractor.get("sort").getString("sort"));
                if (type == null) {
                    throw new DataBaseInfoException("The sort parameter is wrong ("
                            + paramsExtractor.get("sort").getString("sort") + ")"
                    );
                }
                projects = type.getProjectsAndIssuesSorted(paramsExtractor, null, connSupplier);
            }
        }
        List<ProjectDto> list = projects;
        return new CommandResult() {
            @Override
            public String htmlView() {
                return new HtmlProjectsTablePage().htmlProjectsTablePage(list);
            }

            @Override
            public String textPlain() {
                final String[] plain = {""};
                list.forEach(
                        proj -> {
                            plain[0] += "Project id: " + proj.getId() + "\n" +
                                    "name: " + proj.getName() + "\n" +
                                    "description: " + proj.getDescription() + "\n" +
                                    "creation date: " + proj.getCreationDate() + "\n";
                        }
                );
                return plain[0];
            }
        };
    }


    private boolean validParameters(Set<String> param) throws DataBaseInfoException {
        Stream<String> invalidParam = param.stream().filter(elem -> !params.contains(elem));
        if (invalidParam.toArray().length > EMPTY)
            throw new DataBaseInfoException("The parameters were wrongly incorrected");
        return true;
    }
}
