package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.ProjectLabelIssueDto;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static java.sql.Statement.EXECUTE_FAILED;

public class SQLCreateIssueLabel {

    public void createIssueLabel(ProjectLabelIssueDto label, MySupplier connection)
            throws DataBaseInfoException {
        try {
            PreparedStatement ps = connection.get().prepareStatement(
                    "insert into Project_Label_Issue " +
                            "(idIIssue, IdProjLab, IdIProject, idPLLabel) " +
                            "values (?, ?, ?, (select id from Label as l where l.description = ?))"
            );
            for (String labelName : label.getLabels()) {
                ps.setInt(1, label.getIssueID());
                ps.setInt(2, label.getProjectID());
                ps.setInt(3, label.getProjectID());
                ps.setString(4, labelName);
                ps.addBatch();
            }
            int[] res = ps.executeBatch();
            for (int exe : res) {
                if(exe == EXECUTE_FAILED){
                    throw new DataBaseInfoException("Couldn't insert all labels to the issue");
                }
            }
        } catch (SQLException e){
            throw new DataBaseInfoException("Couldn't associate the label to the issue");
        }
    }
}
