package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.IssueDto;
import pt.isel.ls.dataAccess.ProjectDto;
import pt.isel.ls.dataAccess.StatusDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLGetIssueByProjectId {

    public List<IssueDto> getIssueByProjectId(
            int idProj, List<String> labels, MySupplier con
    ) throws DataBaseInfoException {
        try {
            String sql = "select * from Issue ";
            if (labels != null && !labels.isEmpty()){
                sql += "inner join Project_Label_Issue as pl on (pl.idIIssue = i.idIssue) " +
                        "inner join Label as l on (pl.idPLLabel = l.id) " +
                        "where l.description in (?) ";
            }
            sql +=  "where idProject = ?";
            PreparedStatement ps = con.get().prepareStatement(sql);
            if (labels!= null && !labels.isEmpty()) {
                Array array = ps.getConnection().createArrayOf("nvarchar", labels.toArray());
                ps.setArray(1, array);
                ps.setInt(2, idProj);
            } else {
                ps.setInt(1, idProj);
            }
            ResultSet res = ps.executeQuery();
            List<IssueDto> list = new ArrayList<>();
            while (res.next()) {
                list.add(new IssueDto(
                                res.getInt("idIssue"),
                                new ProjectDto(res.getInt("idProject")),
                                new StatusDto(res.getInt("idStatus")),
                                res.getString("issueName"),
                                res.getString("description"),
                                res.getDate("created"),
                                res.getDate("updated")
                        )
                );
            }
            return list;
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't get the specific Issue");
        }

    }
}
