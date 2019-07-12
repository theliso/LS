package pt.isel.ls.dataAccess;

public class StatusDto {
    private int id;
    private String description;

    public int getId() {
        return id;
    }

    public StatusDto(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public StatusDto(int id, String description) {
        this.id = id;
        this.description = description;
    }
}
