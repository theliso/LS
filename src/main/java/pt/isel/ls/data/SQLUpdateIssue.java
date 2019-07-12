package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUpdateIssue {
    public void updateIssue(int projId, int issueId, int state, MySupplier con) throws DataBaseInfoException {
        try {
            PreparedStatement ps = con.get().prepareStatement("update Issue set idStatus = ?, " +
                    "updated = ? where idIssue = ? and idProject = ?");
            Date currentDate = new Date(System.currentTimeMillis());
            ps.setInt(1, state);
            ps.setDate(2, currentDate);
            ps.setInt(3, issueId);
            ps.setInt(4, projId);
            ps.execute();
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't update the issue state.");
        }

    }
}
