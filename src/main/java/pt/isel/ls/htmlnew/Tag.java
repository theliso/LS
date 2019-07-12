package pt.isel.ls.htmlnew;

import pt.isel.ls.dataAccess.ProjectDto;

import java.util.List;

public class Tag {

    public Tag(){}

    public HTML html(Node...elems){
        return new HTML("html", elems);
    }

    public HTML head(Node...elems){
        return new HTML("head", elems);
    }

    public HTML body(Node...elems){
        return new HTML("body", elems);
    }

    public HTML paragraph(Node...elems){
        return new HTML("p", elems);
    }

    public HTML href(Node...elems){return new HTML("a",elems);}

    public HTML table(Node...elems){
        return new HTML("table", elems);
    }

    public HTML tableRow(Node...elems){
        return new HTML("tr", elems);
    }

    public HTML tableData(Node...elems){
        return new HTML("td", elems);
    }

    public HTML tableHead(Node...elems){
        return new HTML("th", elems);
    }

    public HTML form(Node...elems){
        return new HTML("form", elems);
    }

    public HTML input(Node...elems){
        return new HTML("input", elems);
    }

    public HTML style(Node...elems){
        return new HTML("style", elems);
    }

    public HTML h(int size, Node...elems){
        return new HTML(String.join("", "h", String.valueOf(size)), elems);
    }

    public HTML div(Node...elems){
        return new HTML("div", elems);
    }

    public HTML select(Node...elems){
        return new HTML("select", elems);
    }

    public HTML link(Node...elems){
        return new HTML("link", elems);
    }

    public HTML option(Node...elems){
        return new HTML("option", elems);
    }

    public Node br() {
        return new HTML("br");
    }

    public HTML button(Node... elems) {
        return new HTML("button", elems);
    }

    public HTML anchor(Node...elems) {
        return new HTML("a", elems);
    }
}
