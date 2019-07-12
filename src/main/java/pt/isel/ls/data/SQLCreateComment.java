package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.CommentDto;

import java.sql.*;
import java.util.function.Supplier;

public class SQLCreateComment {

    private final int CLOSE = 2;

    public int createComment(CommentDto commentDto, MySupplier con) throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "select idStatus from Issue where idIssue = ?"
            );
            ps.setInt(1, commentDto.getIssue());
            ResultSet res = ps.executeQuery();
            if (res.next()) {
                if (res.getInt("idStatus") == CLOSE) {

                    throw new DataBaseInfoException(
                            "This issue is closed and you can't add comments"
                    );
                }
            }
            ps = con.get().prepareStatement(
                    "insert into Comment (idIssue, idProject, " +
                            " commentDate, text) values (?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, commentDto.getIssue());
            ps.setInt(2, commentDto.getIdProject());
            ps.setDate(3, commentDto.getDate());
            ps.setString(4, commentDto.getText());
            ps.execute();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataBaseInfoException(e.getMessage());
        }
    }
}
