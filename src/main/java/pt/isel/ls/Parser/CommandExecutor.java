package pt.isel.ls.Parser;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.*;
import pt.isel.ls.FileProcess.FileHTMLWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import static pt.isel.ls.Commands.Command.CommandResult;
import static pt.isel.ls.Parser.CommandState.INVALID;
import static pt.isel.ls.Parser.Router.*;

public class CommandExecutor {

    private static final int MAXIMUM_LENGTH = 4;
    private static final int MINIMUM_LENGTH = 2;


    public Connection getConnection() throws SQLException {
        PGSimpleDataSource ds = new PGSimpleDataSource();
        //ds.setUrl(System.getenv("JDBC_DATABASE_URL"));
        //return ds.getConnection();
        //ds.setPortNumber(Integer.parseInt(System.getenv("PORT")));
//        ds.setDatabaseName(System.getenv("DATABASE_NAME"));
//        ds.setUser(System.getenv("PGUSER"));
//        ds.setPassword(System.getenv("PASSWORD"));

        return DriverManager.getConnection(System.getenv("JDBC_DATABASE_URL"));
    }

/*
    public Connection getConnection() throws SQLServerException {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setDatabaseName(System.getenv("DATABASE_NAME"));
        ds.setUser(System.getenv("USER_NAME"));
        ds.setPassword(System.getenv("PASSWORD"));
        return ds.getConnection();
    }
*/
    public boolean run(String[] args)
            throws InvalidCommandException, InvalidParametersException,
            DataBaseInfoException, ConnectionException, ExceededNumberOfArgumentsException {
        Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
        if (args.length < MINIMUM_LENGTH)
            throw new ExceededNumberOfArgumentsException("Too few arguments inputted");
        if (args.length > MAXIMUM_LENGTH)
            throw new ExceededNumberOfArgumentsException("Too much arguments inputted!");
        Connection[] conn = {null};
        Request request = new Request(args);
        if (request.getState() == INVALID)
            throw new InvalidCommandException("The command given was invalid");
        Command cmd = getCommand(request);
        logger.info("Executing command (method: {}, path: {}, headers: {}, parameters: {}",
                request.getMethod(),
                request.getPath(),
                request.getHeader(),
                request.getParameter());
        if (cmd == null)
            throw new NoCommandGivenException("The command inputted was wrong!");
        MySupplier sup = () -> conn[0] = getConnection();
        CommandResult res = cmd.execute(request, sup);
        if (res != null) {
            if (isHTMLText(request.getHeader())) {
                Map<String, Result> headerDetails = getHeaderDetails(request.getHeader());
                String fileName = headerDetails.containsKey("file-name") ?
                        headerDetails.get("file-name").getString("file-name") :
                        null;
                FileHTMLWriter.write(res.htmlView(),fileName);
            } else {
                System.out.println(res.textPlain());
            }
        }
        closeConnection(conn[0]);
        return true;
    }

    private void closeConnection(Connection connection) throws ConnectionException {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            throw new ConnectionException("Couldn't close the connection");
        }
    }
}
