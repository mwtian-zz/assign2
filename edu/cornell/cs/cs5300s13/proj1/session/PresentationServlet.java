package edu.cornell.cs.cs5300s13.proj1.session;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PresentationServlet
 */
@WebServlet("/PresentationServlet")
public class PresentationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private int serverID = 0;
	private SessionManager sessionManager = null;
	
    /**
     * Default constructor. 
     */
    public PresentationServlet() {
        // TODO Auto-generated constructor stub
    }

    public void init() throws ServletException {
    	sessionManager = new SessionManager();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServerSessionState serverState = sessionManager.getSession(request);
		if (serverState == null) {
			serverState = sessionManager.newSession();
		}
		PrintWriter out = response.getWriter();
		printPage(out, request, serverState);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
	
	private void printPage(PrintWriter output, HttpServletRequest request, ServerSessionState serverState) {
		output.println
		("<html>" +
		"<body>" +
		"<br>&nbsp;<br>" +
		"<big><big><b>" +
		"Welcome, New User! (<- Your message goes here)" +
		"<br>&nbsp;<br>" +
		"</b></big></big>" +
		"<form method=GET action=\"PresentationServlet\">" +
		"<input type=submit name=cmd value=Replace>&nbsp;&nbsp;<input type=text name=NewText size=40 maxlength=512>&nbsp;&nbsp;" +
		"</form>" +
		"<form method=GET action=\"PresentationServlet\">" +
		"<input type=submit name=cmd value=Refresh>" +
		"</form>" +
		"<form method=GET action=\"PresentationServlet\">" +
		"<input type=submit name=cmd value=LogOut>" +
		"</form>" +
		"<p>" +
		"Session on: " +
		"IP - " + request.getRemoteAddr() + "; " + 
		"port - " + request.getRemotePort() + "; " +
		"host - " + request.getRemoteHost() +
		"<p>" +
		"Expires on " + serverState.getExpiration().toString() +
		"</body>" +
		"</html>\n");
	}

}
