package pt.isel.ls.htmlnew;

import java.util.Map;

public class CSS extends Node {

    private final String name;
    private final Map<String, String> typeOfStyles;

    public CSS(String name, Map<String, String> typeOfStyles){
        this.name = name;
        this.typeOfStyles = typeOfStyles;
    }

    @Override
    protected String htmlFile() {
        StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("{");
        for (String s : typeOfStyles.keySet()) {
            sb.append(s);
            sb.append(':');
            sb.append(typeOfStyles.get(s));
            sb.append(';');
        }
        sb.append("}");
        return sb.toString();
    }
}
