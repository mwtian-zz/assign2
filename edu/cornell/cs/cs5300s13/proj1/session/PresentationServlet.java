package edu.cornell.cs.cs5300s13.proj1.session;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PresentationServlet
 */
@WebServlet("/home")
public class PresentationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	//private int serverID = 0;
	private SessionMap sessionMap = null;
	
    /**
     * Default constructor. 
     */
    public PresentationServlet() {
    }

    public void init() throws ServletException {
    	sessionMap = new SessionMap();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServerSessionState serverSession = processRequest(request);
		generateResponse(request, response, serverSession);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServerSessionState serverSession = processRequest(request);
		String commmand = request.getParameter("cmd");
		if (commmand != null) {
			if (commmand.equals("Replace")) {
				String text = request.getParameter("NewText");
				if (text == null) {
					text = "";
				}
				serverSession.setMessage(text);
			} else if (commmand.equals("LogOut")) {
				sessionMap.deleteSession(serverSession);
				serverSession = sessionMap.newSession();
			}
		}
		generateResponse(request, response, serverSession);
	}
	
	private ServerSessionState processRequest(HttpServletRequest request) {
		ServerSessionState serverSession = sessionMap.getSession(request);
		if (serverSession == null) {
			serverSession = sessionMap.newSession();
		} else {
			serverSession.updateExpiration();
		}
		return serverSession;
	}
	
	private void generateResponse(HttpServletRequest request, HttpServletResponse response, ServerSessionState serverSession) throws IOException {
		serverSession.setClientSession(response);
		PrintWriter out = response.getWriter();
		Cookie[] cookies = request.getCookies();
		String cookieContent = "";
		if (cookies != null && cookies.length > 0) {
			cookieContent = cookies[0].getValue();
		}
		out.println
		("<html>" +
		"<body>" +
		"<br>&nbsp;<br>" +
		"<big><big><b>" +
		serverSession.getMessage() +
		"<br>&nbsp;<br>" +
		"</b></big></big>" +
		"<form method=POST action=\"home\">" +
		"<input type=submit name=cmd value=Replace>&nbsp;&nbsp;" + 
		"<input type=text name=NewText value=\"" + serverSession.getMessage() + "\" " +
		"size=40 maxlength=512>&nbsp;&nbsp;" +
		"</form>" +
		"<form method=GET action=\"home\">" +
		"<input type=submit name=cmd value=Refresh>" +
		"</form>" +
		"<form method=POST action=\"home\">" +
		"<input type=submit name=cmd value=LogOut>" +
		"</form>" +
		"<p>" +
		"Session ID: " + serverSession.getSessionID() +
		"<p>" +
		"<p>" +
		"Session on: " +
		"IP - " + request.getRemoteAddr() + "; " + 
		"port - " + request.getRemotePort() + "; " +
		"host name - " + request.getRemoteHost() +
		"<p>" +
		"Session expires on " + serverSession.getExpiration().toString() +
		"<p>" +
		"Client cookie content: " + cookieContent +
		"<p>" +
		"</body>" +
		"</html>\n");
	}
}
