package pt.isel.ls.Http;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.MySupplier;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.Parser.Result;

import java.text.MessageFormat;
import java.util.Map;

import static pt.isel.ls.Parser.Router.getPathDetails;

public class HttpServer implements Command {

    // Port TCP used by default if not specified any
    private static final int PORT = 8080;
    private static Logger logger = LoggerFactory.getLogger(HttpServer.class);
    public static void main(String[] args) throws Exception {
        logger.info("Starting main...");
        String port = args[0];
        String portUsed = port != null ? port : String.valueOf(PORT);
        logger.info("Listening on port {]", portUsed);
        Server server = new Server(Integer.parseInt(portUsed));
        ServletHandler handler = new ServletHandler();
        server.setHandler(handler);
        handler.addServletWithMapping(new ServletHolder(new ProjectsServlet()), "/*");
        server.start();
        logger.info("Server started");
        logger.info("http://localhost:{}/", portUsed);
    }

    @Override
    public CommandResult execute(Request params, MySupplier connSupplier)
            throws DataBaseInfoException, InvalidParametersException {
        Map<String, Result> pathDetails = getPathDetails(params.getPath());
        final String[] port = {null};
        pathDetails.keySet().forEach(tcp -> {
            if(!tcp.contentEquals(""))
                port[0] = tcp;
        });
        try {
            main(new String[]{port[0]});
        } catch (Exception e) {
            logger.error("Couldn't start the server. Error: {}", e.getMessage());
            //System.out.println("Couldn't start the server");
        }
        return null;
    }
}
