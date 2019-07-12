package pt.isel.ls.dataAccess;

public class LabelDto {
    private int id;
    private String name;
    private String color;

    public LabelDto( String description) {
        this.name = description;
    }
    public LabelDto(int id, String description) {
        this.id = id;
        this.name = description;
    }
    public LabelDto(String name,String color) {
        this.name = name;
        this.color = color;
    }


    public String getName() {
        return name == null ? "Not Available" : name;
    }

    public String getColor() {
        return color;
    }
}
