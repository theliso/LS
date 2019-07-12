package pt.isel.ls.dataAccess;

public class ProjectLabelDto {

    private int projectID;
    private String labelName;
    private String color;

    public ProjectLabelDto(int projectID, String labelID, String color) {
        this.projectID = projectID;
        this.labelName = labelID;
        this.color = color;
    }

    public int getProjectID() {
        return projectID;
    }

    public String getLabelName() {
        return labelName;
    }

    public String getColor() {
        return color;
    }
}
