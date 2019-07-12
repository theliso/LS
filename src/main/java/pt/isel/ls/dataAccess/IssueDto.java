package pt.isel.ls.dataAccess;

import java.sql.Date;
import java.util.List;

public class IssueDto {
    private int id;
    private ProjectDto projDto;
    private StatusDto statusDto;
    private String name;
    private String description;
    private Date creationDate;
    private Date updateDate;
    private List<CommentDto> comments;
    private List<LabelDto> labels;
    private int nrComments;

    private final String[] HEADERS = {"id","name","description", "creation date", "update date","nr comments", "status"};

    public IssueDto(int id, ProjectDto projDto, StatusDto statusDto,
                    String name, String description, Date creationDate, Date updateDate) {
        this.id = id;
        this.projDto = projDto;
        this.statusDto = statusDto;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        nrComments = 0;
    }

    public IssueDto(int id, ProjectDto projDto, StatusDto statusDto,
                    String name, String description, Date creationDate, Date updateDate,
     List<CommentDto> list, List<LabelDto> labels) {
        this.id = id;
        this.projDto = projDto;
        this.statusDto = statusDto;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.comments = list;
        this.labels = labels;
        nrComments = 0;
    }

    public IssueDto(ProjectDto projDto, StatusDto statusDto,
                    String name, String description, Date creationDate, Date updateDate) {
        this.projDto = projDto;
        this.statusDto = statusDto;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        nrComments = 0;
    }

    public IssueDto(ProjectDto projDto, StatusDto statusDto,
                    String name, String description, Date creationDate, Date updateDate,
                    List<CommentDto> list, List<LabelDto> labels) {
        this.projDto = projDto;
        this.statusDto = statusDto;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.updateDate = updateDate;
        this.comments = list;
        this.labels = labels;
        nrComments = 0;
    }

    public IssueDto(int id) {
        this.id = id;
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

    public ProjectDto getProjDto() {
        return projDto;
    }

    public StatusDto getStatusDto() {
        return statusDto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }


    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public List<LabelDto> getLabels() {
        return labels;
    }

    public void setLabels(List<LabelDto> labels) {
        this.labels = labels;
    }

    public void setNrComments(int nrComments) { this.nrComments = nrComments; }

    public int getNrComments() { return nrComments; }

    public String[] getHEADERS() {
        return HEADERS;
    }
}
