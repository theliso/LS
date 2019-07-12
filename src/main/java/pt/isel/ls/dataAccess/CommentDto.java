package pt.isel.ls.dataAccess;

import java.sql.Date;

public class CommentDto {
    private int idComment;
    private int idProject;
    private Date date;
    private int issueId;
    private String text;
    private boolean previous;
    private boolean next;

    private final String[] HEADERS = {"id","date","text"};

    public int getIdProject() {
        return idProject;
    }


    public Date getDate() {
        return date;
    }

    public CommentDto(int idProject, Date date, int issueId, String text) {
        this.idProject = idProject;
        this.date = date;
        this.issueId = issueId;
        this.text = text;
    }

    public CommentDto(int idComment, int idProject, Date date, int issueId, String text) {
        this.idComment = idComment;
        this.idProject = idProject;
        this.date = date;
        this.issueId = issueId;
        this.text = text;
    }

    public int getIdComment() { return idComment;}

    public String getText() {
        return text == null ? "Not Available" : text;
    }

    public int getIssue() {
        return issueId;
    }

    public String[] getHEADERS() {
        return HEADERS;
    }

    public boolean isPrevious() {
        return previous;
    }

    public void setPrevious(boolean previous) {
        this.previous = previous;
    }

    public boolean isNext() {
        return next;
    }

    public void setNext(boolean next) {
        this.next = next;
    }
}
