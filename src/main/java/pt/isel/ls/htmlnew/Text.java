package pt.isel.ls.htmlnew;

public class Text extends Node {

    private final String text;

    public Text(String text){
        this.text = text;
    }

    @Override
    protected String htmlFile() {
        return text;
    }
}
