package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.ProjectLabelIssueDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLDeleteLabelFromIssue {
    public void DeleteLabelFromIssue(ProjectLabelIssueDto projectLabelIssueDto, MySupplier connSupplier)
    throws DataBaseInfoException {
        try {
            PreparedStatement ps = connSupplier.get().prepareStatement(
                    "delete from dbo.Project_Label_Issue " +
                            "where idIIssue = ? and idProjLab = ? and idIProject = ? " +
                            "and idPLLabel = (select id from dbo.Label as l " +
                            "where l.description = ?)"
            );
            ps.setInt(1, projectLabelIssueDto.getIssueID());
            ps.setInt(2, projectLabelIssueDto.getProjectID());
            ps.setInt(3, projectLabelIssueDto.getProjectID());
            ps.setString(4, projectLabelIssueDto.getLabelName());
            ps.execute();
        }catch (SQLException e){
            throw new DataBaseInfoException("Couldn't delete the label from the issue");
        }
    }
}
