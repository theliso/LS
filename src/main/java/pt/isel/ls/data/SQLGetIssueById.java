package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLGetIssueById {

    public IssueDto getCommentsUsingIdIssue(int idIssue, int idProj, MySupplier con)
            throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "select * from Issue as i " +
                            "left outer join Comment as c on (c.idIssue = i.idIssue)" +
                            "left outer join Project_Label_Issue as pli on (i.idIssue = pli.idIIssue) " +
                            "left outer join Label as l on (l.id = pli.idPLLabel)" +
                            " where i.idIssue = ?  and i.idProject = ?"
            );
            ps.setInt(1, idIssue);
            ps.setInt(2, idProj);
            ResultSet res = ps.executeQuery();
            res.next();
            List<CommentDto> comments = new ArrayList<>();
            List<LabelDto> labels = new ArrayList<>();
            LabelDto label = null;
            final int LABEL_DESCRIPTION = 18;
            IssueDto issue = null;
            do {
                if (issue == null) {
                    issue = new IssueDto(
                            res.getInt("idIssue"),
                            new ProjectDto(res.getInt("idProject")),
                            new StatusDto(res.getInt("idStatus")),
                            res.getString("issueName"),
                            res.getString(5),
                            res.getDate("created"),
                            res.getDate("updated")
                    );
                }
                Date dateComment = res.getDate("commentDate");
                if(dateComment!=null)
                    comments.add(
                        new CommentDto(
                                res.getInt("idComment"),
                                res.getInt("idProject"),
                                dateComment,
                                res.getInt("idIssue"),
                                res.getString("text")
                        )
                );
                label = new LabelDto(res.getString(LABEL_DESCRIPTION));
                boolean toAdd = true;
                for (LabelDto label1 : labels) {
                    if (label1.getName().equals(label.getName())) toAdd = false;
                }
                if(toAdd) labels.add(label);
            } while (res.next());
            issue.setComments(comments);
            issue.setLabels(labels);
            return issue;
        } catch (SQLException e) {
            throw new DataBaseInfoException("There is an error with the ids given");
        }
    }
}
