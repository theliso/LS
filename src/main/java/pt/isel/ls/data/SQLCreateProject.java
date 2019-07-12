package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.ProjectDto;

import java.sql.*;

public class SQLCreateProject {
    public int createProject(ProjectDto pjDto, MySupplier con)
            throws DataBaseInfoException {
        try {
            //todo: currval to get the id inserted recently
            PreparedStatement ps = con.get().prepareStatement(
                    "insert into Project (projectName, description, " +
                    "creationDate) values (?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            Date date = new Date(System.currentTimeMillis());
            ps.setString(1, pjDto.getName());
            ps.setString(2, pjDto.getDescription());
            ps.setDate(3, date);
            ps.execute();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't insert the project");
        }

    }
}
