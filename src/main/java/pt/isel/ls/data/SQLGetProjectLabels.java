package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.LabelDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGetProjectLabels {

    public List<LabelDto> getProjectLabel(int id, MySupplier con)
            throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "select * from Project_Label as pl " +
                            "inner join Label as l on (pl.idLabel = l.id) " +
                            "where pl.idProject = ?"
            );
            ps.setInt(1, id);
            ResultSet res = ps.executeQuery();
            List<LabelDto> labels = new ArrayList<>();
            while (res.next()){
                labels.add(
                        new LabelDto(res.getString("description"))
                );
            }
            return labels;
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't get the labels of the project");
        }
    }
}
