package pt.isel.ls.htmlnew;

public class Attribute {

    private final String name;
    private final String value;

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String htmlFile() {
        return String.format("%s=\"%s\"", name, value);
    }
}
