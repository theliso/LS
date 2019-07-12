package pt.isel.ls.CommandsTest;

import org.junit.Before;
import org.junit.Test;
import pt.isel.ls.Parser.*;

import static org.junit.Assert.*;
import static pt.isel.ls.Parser.Router.*;


public class RequestTest {

    @Before
    public void registCommands() {
        initRouter();
    }


        @Test
        public void shouldTestMethodPathHeaderAndParameterObtention(){
            //Arrange
            String[] methPath = {"POST", "/projects/123/issues/12/comments"};
            String[] methPathHeadParam = {"GET", "/projects/1/issues", "accept:text/plain", "sort=issue-count&direction=desc"};
            Request request1 = new Request(methPath);
            Request request2 = new Request(methPathHeadParam);

            //Act
            String method1 = request1.getMethod();
            String method2 = request2.getMethod();
            String path1 = request1.getPath();
            String path2 = request2.getPath();
            String parameter1 = request1.getParameter();
            String parameter2 = request2.getParameter();
            String header1 = request1.getHeader();
            String header2 = request2.getHeader();

            //Assert
            assertEquals(method1, "POST");
            assertEquals(path1, "/projects/123/issues/12/comments");
            assertNull(parameter1);
            assertNull(header1);

            assertEquals(method2, "GET");
            assertEquals(path2, "/projects/1/issues");
            assertEquals(header2, "accept:text/plain");
            assertEquals(parameter2, "sort=issue-count&direction=desc");

        }

    @Test
    public void shouldTestState(){
        //Arrange
        String[] cmd1 = {"GET", "/projects/123/issues", "accept:text/html"};
        String[] cmd2 = {"GET", "/projects/123/issues", "accept:text/html|file-name:projects.html"};
        String[] cmd3 = {"GET", "/projects/123/issues", "accept:text/plain"};
        String[] cmd4 = {"GET", "/projects/123/issues"};
        String[] cmd5 = {"GET", "/projects/123/issues", "accept:text/html|file-name:projects.html", "label=new-feature&state=open&label=urgent&sort=created"};

        Request cmdExe = new Request(cmd1);
        Request cmdExe2 = new Request(cmd2);
        Request cmdExe3 = new Request(cmd3);
        Request cmdExe4 = new Request(cmd4);
        Request cmdExe5 = new Request(cmd5);

        //Act
        CommandState state1 = cmdExe.getState();
        CommandState state2 = cmdExe2.getState();
        CommandState state3 = cmdExe3.getState();
        CommandState state4 = cmdExe4.getState();
        CommandState state5 = cmdExe5.getState();


        //Assert
        assertNotNull(state1);
        assertNotNull(state2);
        assertNotNull(state3);
        assertNull(state4);
        assertNotNull(state5);
    }

}
