package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.CommentDto;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.dataAccess.ProjectDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGetComment {
    public CommentDto getCommentById(int commentId, int issueId, MySupplier con) throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "select * from Comment where idComment = ? and idIssue = ?"
            );
            ps.setInt(1, commentId);
            ps.setInt(2, issueId);
            ResultSet res = ps.executeQuery();
            boolean next = res.next();
            CommentDto comment = new CommentDto(
                    res.getInt("idComment"),
                    res.getInt("idProject"),
                    res.getDate("commentDate"),
                    res.getInt("idIssue"),
                    res.getString("text")
            );
            ps.setInt(1,commentId+1);
            res = ps.executeQuery();
            if(res.next())
                comment.setNext(true);
            else comment.setNext(false);

            ps.setInt(1,commentId-1);
            res = ps.executeQuery();
            if(res.next())
                comment.setPrevious(true);
            else comment.setPrevious(false);
            return comment;
        } catch (SQLException e) {
            throw new DataBaseInfoException("The comment requested does not exist");
        }
    }

    public boolean hasCommentAdded(int addition, int commentId, int issueId, MySupplier con) {
        try {
            getCommentById(commentId+addition, issueId, con);
            return true;
        } catch (DataBaseInfoException e) {
            return false;
        }
    }
}
