package pt.isel.ls.Http;

import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.ConnectionException;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Parser.CommandExecutor;
import pt.isel.ls.Parser.Request;
import pt.isel.ls.htmlnew.Tag;
import pt.isel.ls.htmlnew.Text;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;

import static pt.isel.ls.Commands.Command.CommandResult;
import static pt.isel.ls.Parser.Router.getCommand;

public class ProjectsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        String query = null;
        if ((query = req.getQueryString()) != null) {
            query = (query.contentEquals("")) ? getFormDataFromIds(req) : query;
            doMethod(resp, req, req.getMethod() + ' ' + req.getRequestURI() + ' ' + query);
        } else {
            doMethod(resp, req, req.getMethod() + ' ' + req.getRequestURI());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        BufferedReader reader = req.getReader();
        String s;
        StringBuilder res = new StringBuilder();
        while ((s = reader.readLine()) != null) {
            res.append(s);
        }
        doMethod(resp, req, req.getMethod() + ' ' + req.getRequestURI() + ' ' + res.toString());
    }

    private String getFormDataFromIds(HttpServletRequest req) {
        return req.getParameter("labels");
    }

    private void doMethod(HttpServletResponse resp, HttpServletRequest req, String uri) {
        OutputStream out;
        final Connection[] conn = {null};
        try {
            out = resp.getOutputStream();
            try {
                String[] uriSplitted = uri.split(" ");
                Request request = new Request(uriSplitted);
                Command command = getCommand(request);
                if (command == null) {
                    throw new InvalidParametersException("Check the url again");
                }
                CommandResult res = command.execute(
                        request,
                        () -> conn[0] = new CommandExecutor().getConnection()
                );
                byte[] bytes;
                if (req.getMethod().contentEquals("POST")){
                    resp.setStatus(HttpStatusCode.SeeOther.valueOf());
                }else {
                    resp.setStatus(HttpStatusCode.Ok.valueOf());
                }
                if (req.getHeader("accept").contentEquals("text/plain")) {
                    bytes = res.textPlain().getBytes();
                } else {
                    bytes = res.htmlView().getBytes();
                }
                resp.setContentLength(bytes.length);
                out.write(bytes);
                out.close();
                resp.encodeRedirectURL(uriSplitted[1]);
            } catch (DataBaseInfoException e) {
                resp.setStatus(HttpStatusCode.NotFound.valueOf());
                String res = constructErrorMessage(resp.getStatus() + " Not Found", e.getMessage());
                out.write(res.getBytes());
                out.close();
            } catch (InvalidParametersException e) {
                resp.setStatus(HttpStatusCode.BadRequest.valueOf());
                String res = constructErrorMessage(resp.getStatus() + " Bad Request", e.getMessage());
                out.write(res.getBytes());
                out.close();
            }
            closeConnection(conn[0]);
        } catch (IOException | ConnectionException e) {
            e.printStackTrace();
        }
    }

    private String constructErrorMessage(String status, String message) {
        Tag html = new Tag();
        return html.html(
                html.head(
                        html.h(
                                1,
                                new Text(status)
                        )
                ),
                html.body(
                     html.paragraph(
                             new Text(message)
                     )
                )
        ).htmlFile();
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
