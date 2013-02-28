package edu.cornell.cs.cs5300s13.proj1.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class SessionMap {
	private int sessionIDCount;
	private LinkedHashMap<Integer, ServerSessionState> map;
	private String cookieName = "CS5300S13PROJ1SESSION";
	private static final int INITIAL_LOAD = 10000;
	private static final int RESILIENCE = 0;
	
	public SessionMap() {
		sessionIDCount = 0;
		map = new LinkedHashMap<Integer, ServerSessionState>(INITIAL_LOAD, 1.1f, true);
	}
	
	// Return an instance of server session only when the session has been
	// stored and has not expired yet.
	// Otherwise return null.
	public synchronized ServerSessionState getSession(HttpServletRequest request) {
		
		Cookie[] clientCookies = request.getCookies();
		ServerSessionState serverSession;
		if (clientCookies != null && clientCookies.length > 0 && cookieName.equals(clientCookies[0].getName())) {
			ClientSessionState clientSession = ClientSessionState.translateCookie(clientCookies[0]);
			int sessionKey = clientSession.getSessionID();
			if (map.containsKey(sessionKey)) {
				serverSession = map.get(sessionKey);
				if (serverSession.getExpiration().after(new Date())) {
					// Update expiration here, so sessions ordered by the expiration time
					// is the same as sessions ordered by the access
					serverSession.updateExpiration();
					return serverSession;
				} else {
					map.remove(sessionKey);
				}
			}
		}
		return null;
	}
	
	public synchronized ServerSessionState newSession(HttpServletRequest request) {
		String[] local = new String[RESILIENCE + 1];
		local[0] = request.getLocalAddr() + ":" + request.getLocalPort();
		ServerSessionState serverSession = new ServerSessionState(++sessionIDCount, local);
		map.put(serverSession.getSessionID(), serverSession);
		return serverSession;
	}
	
	public synchronized void deleteSession(ServerSessionState serverSession) {
		map.remove(serverSession.getSessionID());
	}
	
	public synchronized void deleteExpired() {
		for (Map.Entry<Integer, ServerSessionState> entry : map.entrySet()) {
			// the access order (from least to most recent) is the same as 
			// the expiration order, so the expired items are at the beginning of the list
			if (entry.getValue().getExpiration().after(new Date())) {
				break;
			} else {
				map.remove(entry.getKey());
			}
		}
	}
}
