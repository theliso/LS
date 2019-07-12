package pt.isel.ls.data;

import pt.isel.ls.Commands.SubCommands.Gets.SortType;
import pt.isel.ls.Commands.SubCommands.Gets.WithParameters.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SQLGet {

    private static final Map<String, Supplier<SortType>> parameters = new HashMap<>();

    static{
        addParameters();
    }

    public SQLGet() {
    }

    private static void addParameters() {
        parameters.put("creation-date", GetProjectByCreationDate::new);
        parameters.put("issue-count", GetProjectByIssueCount::new);
        parameters.put("open-issue-count", GetProjectByIssueCountState::new);
        parameters.put("closed-issue-count", GetProjectByIssueCountState::new);
        parameters.put("created", GetIssueSortedByDate::new);
        parameters.put("updated", GetIssueSortedByDate::new);
        parameters.put("comments", GetIssueSortedByComments::new);
        parameters.put("open", GetIssueFiltered::new);
        parameters.put("closed", GetIssueFiltered::new);
        parameters.put("all", GetIssueSortedByAll::new);
        parameters.put("label", GetIssueFilteredByLabel::new);
    }

    public static SortType getSortParameter(String sortParam){
        return parameters.get(sortParam).get();
    }

    public static SortType getStateParameter(String stateParam){
        return parameters.get(stateParam).get();
    }


}
