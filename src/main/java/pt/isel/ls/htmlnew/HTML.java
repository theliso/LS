package pt.isel.ls.htmlnew;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class HTML extends Node {

    private final String file;
    private final List<Attribute> attributes = new LinkedList<>();
    private final List<Node> elems;
    private static int tab = 0;

    public HTML(String name, Node...tags){
        file = name;
        elems = Arrays.asList(tags);
    }

    public HTML withAttributes(String name, String value){
        attributes.add(new Attribute(name, value));
        return this;
    }

    @Override
    public String htmlFile(){
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        sb.append(file);
        for (Attribute attribute : attributes) {
            sb.append(' ');
            sb.append(attribute.htmlFile());
        }
        sb.append(">");
        sb.append(tabs(++tab));
        for (int i = 0; i < elems.size(); ++i) {
            sb.append(elems.get(i).htmlFile());
            sb.append(" ");
            if(i<elems.size()-1)
                sb.append(tabs(tab));
        }
        sb.append(tabs(--tab));
        sb.append(String.format("</%s>", file));
        return sb.toString();
    }

    private String tabs(int count) {
        if(count==0)return "\n";
        else{
            String str="\n";
            for(int i =0; i<count;++i){
                str += "\t";
            }
            return str;
        }
    }
}
