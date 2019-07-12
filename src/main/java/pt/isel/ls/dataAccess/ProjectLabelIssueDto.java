package pt.isel.ls.dataAccess;

import java.util.List;

public class ProjectLabelIssueDto {

    private int projectID;
    private int issueID;
    private List<String> labels;
    private String labelName;

    public ProjectLabelIssueDto(int projectID, int issueID, String labelName) {
        this.projectID = projectID;
        this.issueID = issueID;
        this.labelName = labelName;
    }

    public ProjectLabelIssueDto(int projectID, int issueID, List<String> labelName) {
        this.projectID = projectID;
        this.issueID = issueID;
        this.labels = labelName;
    }

    public int getProjectID() {
        return projectID;
    }

    public int getIssueID() {
        return issueID;
    }

    public List<String> getLabels() {
        return labels;
    }

    public String getLabelName() {
        return labelName;
    }
}
