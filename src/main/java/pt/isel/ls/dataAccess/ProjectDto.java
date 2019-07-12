package pt.isel.ls.dataAccess;

import java.sql.Date;
import java.util.List;

public class ProjectDto {
    private int id;
    private String name;
    private String description;
    private List<LabelDto> labels;
    private Date creationDate;
    private final String[] HEADERS = {"id","name","description","creationDate"};
    public ProjectDto(int id) {
        this.id = id;
    }

    public ProjectDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ProjectDto(int id, String name, String description, Date creationDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
    }

    public ProjectDto(int id, String name, String description, List<LabelDto> labels) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.labels = labels;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description == null ? "Not Available" : description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<LabelDto> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelDto> labels) {
        this.labels = labels;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public String[] getHEADERS() {
        return HEADERS;
    }
}