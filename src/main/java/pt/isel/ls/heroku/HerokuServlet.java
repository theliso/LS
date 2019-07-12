package pt.isel.ls.heroku;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.isel.ls.Commands.Command;
import pt.isel.ls.Exception.DataBaseInfoException;
import pt.isel.ls.Exception.InvalidParametersException;
import pt.isel.ls.Http.HttpStatusCode;
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

import static pt.isel.ls.Parser.Router.getCommand;

public class HerokuServlet extends HttpServlet {

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
        String postText;
        StringBuilder buffer = new StringBuilder();
        while ((postText = reader.readLine()) != null) {
            buffer.append(postText);
        }
        doMethod(resp, req, req.getMethod() + ' ' + req.getRequestURI() + ' ' + buffer.toString());
    }

    private String getFormDataFromIds(HttpServletRequest req) {
        return req.getParameter("labels");
    }

    private void doMethod(HttpServletResponse resp, HttpServletRequest req, String uri) {
        Logger logger = LoggerFactory.getLogger(HerokuServlet.class);
        OutputStream out;
        try {
            out = resp.getOutputStream();
            try {
                String[] uriSplitted = uri.split(" ");
                Request request = new Request(uriSplitted);
                Command command = getCommand(request);
                if (command == null) {
                    throw new InvalidParametersException("Check the url again");
                }
                Command.CommandResult res = command.execute(
                        request,
                        () -> new CommandExecutor().getConnection()
                );
                byte[] bytes;
                resp.setStatus(HttpStatusCode.Ok.valueOf());
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
                logger.error(e.getMessage());
                resp.setStatus(HttpStatusCode.NotFound.valueOf());
                String res = constructErrorMessage(resp.getStatus() + " Not Found", e.getMessage());
                out.write(res.getBytes());
                out.close();
            } catch (InvalidParametersException e) {
                logger.error(e.getMessage());
                resp.setStatus(HttpStatusCode.BadRequest.valueOf());
                String res = constructErrorMessage(resp.getStatus() + " Bad Request", e.getMessage());
                out.write(res.getBytes());
                out.close();
            }
        } catch (IOException e) {
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

}
