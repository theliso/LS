package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.IssueDto;
import pt.isel.ls.dataAccess.LabelDto;
import pt.isel.ls.dataAccess.ProjectDto;
import pt.isel.ls.dataAccess.StatusDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLGetIssueSortedBy {

    public List<IssueDto> getIssueFilteredByLabel(
            List<String> labels, String direction, MySupplier con
    ) throws DataBaseInfoException {
        try {
            String[] sql = {"select * from Issue as i " +
                    "inner join Project_Label_Issue as pl on (pl.idIIssue = i.idIssue) " +
                    "inner join Label as l on (pl.idPLLabel = l.id) " +
                    "where l.description in ("};
            labels.forEach(elem -> sql[0] += "?,");
            String query = sql[0].substring(0, sql[0].length() - 1);
            query += ") order by i.idIssue ";
            PreparedStatement ps = con.get().prepareStatement(
                    query + direction
            );
            for (int i = 0; i < labels.size(); ++i) {
                ps.setString(i + 1, labels.get(i));
            }
            ResultSet res = ps.executeQuery();
            List<IssueDto> list = new ArrayList<>();
            List<LabelDto> label = new ArrayList<>();
            IssueDto issue = null;
            while (res.next()) {
                if (issue == null) {
                    issue = new IssueDto(
                            res.getInt("idIssue"),
                            new ProjectDto(res.getInt("idProject")),
                            new StatusDto(res.getInt("idStatus")),
                            res.getString("issueName"),
                            res.getString("description"),
                            res.getDate("created"),
                            res.getDate("updated")
                    );
                }
                label.add(
                        new LabelDto(
                                res.getInt("id"),
                                res.getString("description")
                        )
                );
                issue.setLabels(label);
            }
            list.add(issue);
            return list;
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't get the specific issue");
        }
    }


    public List<IssueDto> getIssueSortedByState(
            int idProj, Integer state, String direction, List<String> labels, MySupplier con
    ) throws DataBaseInfoException {
        try {
            String sql = "select * from Issue as i ";
            if (labels != null && !labels.isEmpty()) {
                sql += "inner join Project_Label_Issue as pl on (pl.idiissue = i.idissue) " +
                        "inner join Label as l on (pl.idpllabel = l.id) " +
                        "where l.description in (";
                for (String lb : labels) {
                    sql += "?,";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql += ") ";
            }
            String status = (state == null || state == -1) ? "" : "and idStatus = ? ";
            sql += "where idProject = ? " + status +
                    "order by i.idIssue " + direction;
            PreparedStatement ps = con.get().prepareStatement(sql);
            setPreparedStatement(idProj, state, labels, ps);
            ResultSet res = ps.executeQuery();
            List<IssueDto> list = new ArrayList<>();
            List<LabelDto> label = new ArrayList<>();
            setIssueList(labels, res, list, label, false);
            return list;
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't get the specific issue");
        }

    }

    private void setIssueList(
            List<String> labels, ResultSet res, List<IssueDto> list, List<LabelDto> label, boolean comments
    ) throws SQLException {
        while (res.next()) {
            IssueDto issue = new IssueDto(
                    res.getInt("idIssue"),
                    new ProjectDto(res.getInt("idProject")),
                    new StatusDto(res.getInt("idStatus")),
                    res.getString("issueName"),
                    res.getString("description"),
                    res.getDate("created"),
                    res.getDate("updated")
            );
            if (comments) {
                issue.setNrComments(res.getInt("cnt"));
            }
            if (labels != null && !labels.isEmpty()) {
                label.add(
                        new LabelDto(
                                res.getInt("id"),
                                res.getString("description")
                        )
                );
                issue.setLabels(label);
            }
            list.add(issue);
        }
    }

    private void setPreparedStatement(int idProj, Integer state, List<String> labels, PreparedStatement ps)
            throws SQLException {
        if (labels != null && !labels.isEmpty()) {
            int idx;
            for (idx = 0; idx < labels.size(); idx++) {
                ps.setString(idx + 1, labels.get(idx));
            }
            ps.setInt(++idx, idProj);
            if (state != null && state != -1)
                ps.setInt(++idx, state);
        } else {
            ps.setInt(1, idProj);
            if (state != null && state != -1)
                ps.setInt(2, state);
        }
    }

    public List<IssueDto> getIssueSortedByNrOfComments
            (int idProj, Integer state, String direction, List<String> labels, MySupplier con
            ) throws DataBaseInfoException {
        try {

            String sql = "select * from Issue as i " +
                    "inner join ( " +
                    "select idIssue, count(*) as cnt from Comment where idProject = ?" +
                    " group by idIssue) as cmt on (i.idIssue = cmt.idIssue) ";
            if (labels != null && !labels.isEmpty()) {
                sql += " inner join Project_Label_Issue as pl on (pl.idIIssue = i.idIssue) " +
                        "inner join Label as l on (pl.idPLLabel = l.id) " +
                        "where l.description in (";
                for (String lb : labels) {
                    sql += "?,";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql += ") ";
                if (state == -1 || state == null)
                    sql += "and idProject = ?" +
                            "order by i.idIssue " + direction;
                else
                    sql += "and idProject = ? and idStatus = ? " +
                            "order by i.idIssue " + direction;
            } else {
                sql += (state == null || state == -1) ? "" : "where idStatus = ? ";
            }
            sql += "order by cmt.cnt " + direction;
            PreparedStatement ps = con.get().prepareStatement(sql);
            ps.setInt(1, idProj);
            if (labels != null && !labels.isEmpty()) {
                int idx;
                for (idx = 0; idx < labels.size(); idx++) {
                    ps.setString(idx + 2, labels.get(idx));
                }
                if (state != null && state != -1)
                    ps.setInt(++idx, state);
            } else {
                if (state != null && state != -1)
                    ps.setInt(2, state);
            }
            ResultSet res = ps.executeQuery();
            List<IssueDto> list = new ArrayList<>();
            List<LabelDto> label = new ArrayList<>();
            setIssueList(labels, res, list, label, true);
            return list;
        } catch (
                SQLException e) {
            throw new DataBaseInfoException("Couldn't get the specific issue");
        }
    }

    public List<IssueDto> getIssueSortedByDate(
            int idProj, String sort, Integer state, String direction, List<String> labels, MySupplier con
    ) throws DataBaseInfoException {
        try {
            String sql = "select * from Issue as i";
            if (labels != null && !labels.isEmpty()) {
                sql += " inner join Project_Label_Issue as pl on (pl.idIIssue = i.idIssue) " +
                        "inner join Label as l on (pl.idPLLabel = l.id) " +
                        "where l.description in (";
                for (String lb : labels) {
                    sql += "?,";
                }
                sql = sql.substring(0, sql.length() - 1);
                sql += ") ";
                if (state == -1 || state == null)
                    sql += "and idProject = ?" +
                            "order by i." + sort + " " + direction;
                else
                    sql += "and idProject = ? and idStatus = ? " +
                            "order by " + sort + " " + direction;
            } else {
                if (state == null || state == -1)
                    sql += " where idProject = ? " +
                            "order by i." + sort + " " + direction;
                else
                    sql += " where idProject = ? and idStatus = ? " +
                            "order by i." + sort + " " + direction;
            }
            PreparedStatement ps = con.get().prepareStatement(sql);
            setPreparedStatement(idProj, state, labels, ps);
            ResultSet res = ps.executeQuery();
            List<IssueDto> list = new ArrayList<>();
            while (res.next()) {
                list.add(
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
            return list;
        } catch (SQLException e) {
            throw new DataBaseInfoException("Couldn't get the specific issue");
        }

    }
}
