package pt.isel.ls.htmlnew;

public enum CSSValues {

    red("red"), blue("blue"), orange("orange"), stylesheet("stylesheet"), textCSS("text/css"), themeCSS("../../css/theme.css")
    ;


    private final String value;
    CSSValues(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
