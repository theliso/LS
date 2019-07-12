package pt.isel.ls.HtmlPages;

import pt.isel.ls.dataAccess.ProjectDto;
import pt.isel.ls.htmlnew.*;

import java.util.List;

public class HtmlProjectsTablePage {
    private static Tag html = new Tag();

    public String htmlProjectsTablePage(List<ProjectDto> projs) {
        return html.html(
                html.head(
                        new HTML("link")
                                .withAttributes("href", "/style")
                                .withAttributes("media", "screen")
                                .withAttributes("rel", "stylesheet")
                                .withAttributes("type", "text/css")
                ),
                html.body(
                        html.paragraph(
                                html.href(
                                        html.h(1, new Text("Home"))
                                ).withAttributes("href", "/")
                        ),
                        inserTable(projs),
                        html.br(),
                        setFormToAddProject()
                )
        ).htmlFile();

    }

    private Node setFormToAddProject() {

        return html.form(
                new Text("Name text"),
                html.input()
                        .withAttributes("type", "text")
                        .withAttributes("name", "name"),
                new Text("Description text"),
                html.input()
                        .withAttributes("type", "text")
                        .withAttributes("name", "description"),
                html.button(
                        new Text(
                                "Submit"
                        )
                ).withAttributes("type", "submit")
        ).withAttributes("action", "/projects")
                .withAttributes("method", "POST");
    }

    private Node createTable(List<ProjectDto> list) {
        return html.div(
                html.div(
                        html.div(
                                inserTable(list)
                        ).withAttributes("class", "wrap-table100")
                ).withAttributes("class", "container-table100")

        ).withAttributes("class", "limiter");
    }

    private Node inserTable(List<ProjectDto> list) {
        Node[] nodes = new Node[list.size() + 1];
        nodes[0] = inserTableHeaders(list.get(0).getHEADERS());
        System.arraycopy(insertTableData(list), 0, nodes, 1, list.size());
        return html.table(nodes);
    }

    private Node inserTableHeaders(String[] headers) {
        Node[] nodes = new Node[headers.length];
        for (int i = 0; i < headers.length; ++i) {
            nodes[i] = new Text(headers[i]);
        }
        return html.tableHead(nodes);
    }

    private Node[] insertTableData(List<ProjectDto> list) {
        Node[] nodes = new Node[list.size()];
        ProjectDto proj;
        for (int i = 0; i < list.size(); ++i) {
            proj = list.get(i);
            nodes[i] = html.tableRow(
                    html.tableData(
                            html.href(
                                    new Text("" + proj.getId())).withAttributes("href", "/projects/" + proj.getId()),
                            new Text(proj.getName()),
                            new Text(proj.getDescription()),
                            new Text("" + proj.getCreationDate())
                    )
            );

        }
        return nodes;
    }

}
