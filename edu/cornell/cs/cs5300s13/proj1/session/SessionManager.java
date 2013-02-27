package edu.cornell.cs.cs5300s13.proj1.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
	private AtomicInteger sessionCount;
	private ConcurrentHashMap<Integer, ServerSessionState> map;
	private String cookieName = "CS5300S13PROJ1SESSION";
	
	public SessionManager() {
		sessionCount = new AtomicInteger();
		map = new ConcurrentHashMap<Integer, ServerSessionState>();
	}
	
	public ServerSessionState getSession(HttpServletRequest request) {
		Cookie[] clientCookies = request.getCookies();
		if (clientCookies != null && clientCookies.length > 0 && cookieName.equals(clientCookies[0].getName())) {
			ClientSessionState clientSession = SessionManager.translateCookie(clientCookies[0]);
			if (map.containsKey(clientSession.getSessionID())) {
				return map.get(clientSession.getSessionID());
			}
		}
		return null;
	}
	
	public ServerSessionState newSession() {
		ServerSessionState serverSession = new ServerSessionState(sessionCount.incrementAndGet());
		serverSession.setMessage("Welcome, New User! (<- Your message goes here)");
		map.put(serverSession.getSessionID(), serverSession);
		return serverSession;
	}

	public static ClientSessionState translateCookie(Cookie cookie) {
		String[] fields = cookie.getValue().split("_");
		ClientSessionState ss = null;
		if (fields.length >= 4) {
			ss = new ClientSessionState(fields);
		}
		return ss;
	}
}
