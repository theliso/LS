package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.IssueDto;
import pt.isel.ls.dataAccess.ProjectDto;
import pt.isel.ls.dataAccess.StatusDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLGetIssuesByLabel {

    public List<IssueDto> getIssuesByLabel(int projectId, String labelName, MySupplier con)
            throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement(
                    "select * from Issue as i " +
                            "inner join Project_Label_Issue as pli on (i.idIssue = pli.IdIIssue) " +
                            "inner join Label as l on (pli.IdPLLabel = l.id)" +
                            "where i.idProject = ? and l.description = ?"
            );
            ps.setInt(1, projectId);
            ps.setString(2, labelName);
            ResultSet res = ps.executeQuery();
            List<IssueDto> issues = new ArrayList<>();
            while (res.next()){
                issues.add(
                        new IssueDto(
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
            return issues;
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't get the issues with this labelName");
        }
    }

}
