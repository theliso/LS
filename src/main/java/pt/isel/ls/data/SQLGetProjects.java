package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.ProjectDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGetProjects {
    public List<ProjectDto> getProjects(MySupplier con)
            throws DataBaseInfoException {
        try {
            List<ProjectDto> projDto = new ArrayList<>();
            PreparedStatement ps = con.get().prepareStatement("select * from Project");
            ResultSet res = ps.executeQuery();
            while (res.next()) {
                projDto.add(
                        new ProjectDto(
                                res.getInt("id"),
                                res.getString("projectName"),
                                res.getString("description"),
                                res.getDate("creationDate")
                        )
                );
            }
            return projDto;
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't get the projects");
        }
    }
}
