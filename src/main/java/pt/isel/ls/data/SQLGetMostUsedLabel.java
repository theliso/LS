package pt.isel.ls.data;

import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.dataAccess.LabelDto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLGetMostUsedLabel {
    public LabelDto getMostUsedLabel(int id, MySupplier con) throws DataBaseInfoException {
        try{
            PreparedStatement ps = con.get().prepareStatement(
                    "select * from Project_Label as i" +
                            " inner join ("+
                            " select idPLLabel, count(*) as occr from Project_Label_Issue where idIProject = ? "+
                            " group by idPLLabel) as cmt on (i.idLabel = cmt.idPLLabel) inner join" +
                            " (select id,description from Label) as l on (i.idLabel = l.id)  order  by occr DESC"

            );
            ps.setInt(1,id);
            ResultSet res = ps.executeQuery();
            if(!res.next())
                throw new DataBaseInfoException("This project has no labels");

            return new LabelDto(res.getInt("idLabel"),res.getString("description"));

        }catch (SQLException e){
            throw new DataBaseInfoException("This project has no labels");
        }
    }
}
