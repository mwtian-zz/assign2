package edu.cornell.cs.cs5300s13.proj1.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
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
	
	/* Return an instance of server session only when the session has been
	 * stored and has not expired yet.
	 * Otherwise return null.
	 */
	public ServerSessionState getSession(HttpServletRequest request) {
		
		Cookie[] clientCookies = request.getCookies();
		ServerSessionState serverSession;
		if (clientCookies != null && clientCookies.length > 0 && cookieName.equals(clientCookies[0].getName())) {
			ClientSessionState clientSession = SessionManager.translateCookie(clientCookies[0]);
			int sessionKey = clientSession.getSessionID();
			if (map.containsKey(sessionKey)) {
				serverSession = map.get(sessionKey);
				if (serverSession.getExpiration().after(new Date())) {
					return serverSession;
				} else {
					map.remove(sessionKey);
				}
			}
		}
		return null;
	}
	
	public ServerSessionState newSession() {
		ServerSessionState serverSession = new ServerSessionState(sessionCount.incrementAndGet());		
		map.put(serverSession.getSessionID(), serverSession);
		return serverSession;
	}
	
	public void deleteSession(ServerSessionState serverSession) {
		map.remove(serverSession.getSessionID());
	}

	public static ClientSessionState translateCookie(Cookie cookie) {
		String[] fields = cookie.getValue().split("_");
		ClientSessionState clientSession = null;
		if (fields.length >= 4) {
			clientSession = new ClientSessionState(fields);
		}
		return clientSession;
	}
}
