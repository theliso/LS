package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.dataAccess.ProjectLabelDto;

import java.sql.*;

public class SQLAssociateLabelToProject {


    public void associateLabelToProject(ProjectLabelDto projLabelDto, MySupplier connection)
            throws DataBaseInfoException {
        try {
            PreparedStatement ps = connection.get().prepareStatement(
                    "select id from Label where description = ?"
            );
            ps.setString(1, projLabelDto.getLabelName());
            ResultSet res = ps.executeQuery();
            int idLabel = 0;
            if (!res.next()){
                ps = connection.get().prepareStatement(
                        "insert into Label (description) values (?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, projLabelDto.getLabelName());
                ps.executeQuery();
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idLabel = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creating issue failed.");
                    }
                }
            }
            else idLabel = res.getInt("id");
            /*
            "insert into dbo.Project_Label "
                    + "(idProject, idLabel, color) values "
                    + "(?, " +
                    "(select id from dbo.Label as l where l.description = ?)," +
                    "?)"
                    */
            ps = connection.get().prepareStatement(
                    "insert into Project_Label "
                            + "(idProject, idLabel, color) values (?,?,?)"
            );
            ps.setInt(1, projLabelDto.getProjectID());
            ps.setInt(2, idLabel);
            ps.setString(3, projLabelDto.getColor());
            ps.execute();
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't associate the label to the project");
        }
    }
}
