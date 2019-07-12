package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.IssueDto;

import java.sql.*;

public class SQLCreateIssue {
    public int createIssue(IssueDto isDto, MySupplier con) throws DataBaseInfoException {

        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "insert into Issue (idProject, idStatus, issueName, description, " +
                    "created, updated) values (?,?,?,?,?,?)",
                            Statement.RETURN_GENERATED_KEYS
            );
            ps.setInt(1, isDto.getProjDto().getId());
            ps.setInt(2, isDto.getStatusDto().getId());
            ps.setString(3, isDto.getName());
            ps.setString(4, isDto.getDescription());
            ps.setDate(5, isDto.getCreationDate());
            ps.setDate(6, isDto.getUpdateDate());
            ps.execute();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating issue failed.");
                }
            }
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't insert the issue");
        }

    }
}
