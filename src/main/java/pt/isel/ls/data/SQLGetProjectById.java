package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.dataAccess.ProjectDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGetProjectById {
    public ProjectDto getProjectAndLabelsById(int id, MySupplier con) throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "select p.*, l.description as label from Project as p " +
                            "left outer join Project_Label as pl on (p.id = pl.idProject) " +
                            "inner join Label as l on (pl.idLabel = l.id)" +
                            "where p.id = ?"
            );
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            List<LabelDto> labels = new ArrayList<>();
            ProjectDto project = null;
            boolean status = res.next();
            if(!status) {
                return getProjectById(id, con);
            }
            while (status){
                if (project == null){
                    project = new ProjectDto(
                            res.getInt("id"),
                            res.getString("projectName"),
                            res.getString("description"),
                            res.getDate("creationDate")
                    );
                }
                labels.add(
                        new LabelDto(
                                res.getString("label")
                        )
                );
                status = res.next();
            }
            project.setLabels(labels);
            return project;
        } catch (SQLException e) {
            throw new DataBaseInfoException("The project with id = " + id + " it doesn't exist!");
        }
    }

    public ProjectDto getProjectById(int id, MySupplier con) throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "select * from Project where Project.id = ?"
            );
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            res.next();
            return new ProjectDto(
                            res.getInt("id"),
                            res.getString("projectName"),
                            res.getString("description"),
                            res.getDate("creationDate")
                    );
        } catch (SQLException e) {
            throw new DataBaseInfoException("The project with id = " + id + " it doesn't exist!");
        }
    }
}
