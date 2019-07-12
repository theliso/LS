package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.ProjectDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLGetProjectsSortedBy {


    public List<ProjectDto> getProjectsSortedByIssueState(int state, String direction, MySupplier con)
            throws DataBaseInfoException {
        try {
            List<ProjectDto> projDto = new ArrayList<>();
            PreparedStatement ps = con.get().prepareStatement(
                    "select * from Project as p " +
                            "inner join (select idProject, count(*) as cnt " +
                            "from Issue " +
                            "where idStatus = ? " +
                            "group by idProject) as isc " +
                            "on (p.id = isc.idProject) " +
                            "order by isc.cnt " +
                            direction
            );
            ps.setInt(1, state);
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

    public List<ProjectDto> getProjectsSortedByIssueCount(String direction, MySupplier con)
            throws DataBaseInfoException {
        try {
            List<ProjectDto> projDto = new ArrayList<>();
            PreparedStatement ps = con.get().prepareStatement("select * from Project as p " +
                    "inner join (select idProject, count(*) as cnt " +
                    "from Issue " +
                    "group by idProject) as isc " +
                    "on (p.id = isc.idProject) " +
                    "order by isc.cnt " +
                    direction);
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

    public List<ProjectDto> getProjectsSortedByDate(String direction, MySupplier con)
            throws DataBaseInfoException {
        try {
            List<ProjectDto> projDto = new ArrayList<>();
            PreparedStatement ps = con.get().prepareStatement("select * from Project as p " +
                    "order by p.creationDate " + direction);
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
