package pt.isel.ls.Commands.SubCommands.Gets;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Commands.CommandExtractor;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;
import pt.isel.ls.data.SQLGetIssueByProjectId;
import pt.isel.ls.dataAccess.IssueDto;
import pt.isel.ls.htmlnew.*;

import java.util.*;
import java.util.stream.Stream;

import static pt.isel.ls.Parser.Router.getPathDetails;
import static pt.isel.ls.data.SQLGet.getSortParameter;
import static pt.isel.ls.data.SQLGet.getStateParameter;

public class GetIssue extends CommandExtractor implements Command {

    private final int EMPTY = 0;
    private static ArrayList<String> params = new ArrayList<>();

    static {
        params.add("sort");
        params.add("state");
        params.add("direction");
        params.add("label");
    }

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        Map<String, Result> paramExtracted = getParametersExtracted(params.getParameter());
        List<IssueDto> list;
        if (paramExtracted == null || paramExtracted.isEmpty()) {
            SQLGetIssueByProjectId sql = new SQLGetIssueByProjectId();
            list = sql.getIssueByProjectId(
                    pathDetails.get("projects").getInt("projects"),
                    null,
                    connSupplier
            );
        } else {
            if (!validParameters(paramExtracted.keySet())) {
                return null;
            }
            list = getIssuesSorted(paramExtracted, pathDetails, connSupplier);
        }
        return new CommandResult() {
            //TODO verify empty list and change it to adapt to forms
            Tag htmlFile = new Tag();
            @Override
            public String htmlView() {
                if(list==null) {
                    return setNullIssuePage();
                }
                    return htmlFile.html(
                            htmlFile.head(
                                    htmlFile.h(
                                            1,
                                            new Text("Project: " +
                                                    pathDetails.get("projects").getInt("projects")
                                            )
                                    ),
                                    htmlFile.href(
                                            htmlFile.h(2, new Text("Projects"))
                                    ).withAttributes("href", "/projects/")
                            ),
                            htmlFile.body(
                                    htmlFile.table(
                                            htmlFile.tableRow(
                                                htmlFile.tableHead(new Text("id")),
                                                htmlFile.tableHead(new Text("name")),
                                                htmlFile.tableHead(new Text("description")),
                                                htmlFile.tableHead(new Text("creation date")),
                                                htmlFile.tableHead(new Text("nr Comments"))
                                            ),
                                            getTrsFromList(htmlFile, list)
                                    ).withAttributes("class", "limiter")),
                            setForm()
                    ).htmlFile();
            }

            private Node setForm() {
                return htmlFile.form(
                        new Text("Create issue: name -> "),
                        htmlFile.input()
                                .withAttributes("type", "text")
                                .withAttributes("name", "name"),
                        new Text(" description -> "),
                        htmlFile.input()
                                .withAttributes("type", "text")
                                .withAttributes("name", "description"),
                        htmlFile.button(
                                new Text(
                                        "Submit"
                                )
                        ).withAttributes("type", "submit")
                ).withAttributes(
                        "action",
                        "/projects/" + pathDetails.get("projects").getInt("projects") +
                                "/issues"
                ).withAttributes("method", "POST");
            }

            @Override
            public String textPlain() {
                final String[] plain = {"Projects [id: " + list.get(0).getProjDto().getId() + "]\n"};
                list.forEach(
                        issue ->
                                plain[0] += "Issue id: " + issue.getId() + "\n" +
                        "name: " + issue.getName()+ "\n" +
                        "description: " + issue.getDescription()+ "\n" +
                        "creation date: " + issue.getCreationDate()+ "\n" +
                        "nr comments: "+issue.getNrComments()
                );
                return plain[0];
            }

            private String setNullIssuePage() {
                return htmlFile.html(
                        htmlFile.head(
                                htmlFile.href(
                                        new Text(
                                                "/projects/"
                                        ),
                                        htmlFile.h(2,
                                                new Text("Projects")
                                        )
                                )
                        ),
                        htmlFile.body(
                                htmlFile.paragraph(
                                        new Text("there is no comments associated with this issue")
                                )
                        )
                ).htmlFile();
            }
        };
    }

    private Node getTrsFromList(Tag htmlFile, List<IssueDto> list) {
        Node[] listNode = new Node[list.size()];
        for(int i = 0; i<list.size(); i++) {
            IssueDto issue = list.get(i);
            listNode[i] = htmlFile.tableRow(
                    htmlFile.tableData(
                            htmlFile.href(
                                    new Text(String.valueOf(issue.getId()))
                            ).withAttributes("href", "/projects/" + issue.getProjDto().getId() + "/issues/" + issue.getId())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getName())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getDescription())
                    ),
                    htmlFile.tableData(
                            new Text(issue.getCreationDate().toString())
                    ),
                    htmlFile.tableData(
                            new Text(String.valueOf(issue.getNrComments()))
                    )
            );
        }
        return htmlFile.div(listNode);
    }



    private List getIssuesSorted(
            Map<String, Result> params, Map<String, Result> pathDetails, MySupplier connSupplier
    ) throws DataBaseInfoException {
        SortType type;
        if (params.containsKey("sort")) {
            type = getSortParameter(params.get("sort").getString("sort"));
            if (type == null) {
                throw new DataBaseInfoException("The sort parameter is wrong ("
                        + params.get("sort").getString("sort") + ")"
                );
            }
            return type.getProjectsAndIssuesSorted(params, pathDetails, connSupplier);
        }
        if (params.containsKey("state")) {
            type = getStateParameter(params.get("state").getString("state"));
            if (type == null) {
                throw new DataBaseInfoException("The state parameter is wrong ("
                        + params.get("state").getString("state") + ")"
                );
            }
            return type.getProjectsAndIssuesSorted(params, pathDetails, connSupplier);
        }
        if (params.containsKey("label")) {
            type = getSortParameter("label");
            if (type == null) {
                throw new DataBaseInfoException("The state parameter is wrong ("
                        + params.get("label").getString("label") + ")"
                );
            }
            return type.getProjectsAndIssuesSorted(params, pathDetails, connSupplier);
        }
        return null;
    }

    private boolean validParameters(Set<String> param) throws DataBaseInfoException {
        Stream<String> invalidParams = param.stream().filter(elem -> !params.contains(elem));
        if (invalidParams.toArray().length > EMPTY) {
            throw new DataBaseInfoException("The parameters were wrongly inputted!");
        }
        return true;
    }


}
