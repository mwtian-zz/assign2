package edu.cornell.cs.cs5300s13.proj1.session;

import java.util.Date;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class ServerSessionState extends ClientSessionState{
	private Date expiration;
	private String message = "Welcome, New User! (Replace your message below)";
	private static final String cookieName = "CS5300S13PROJ1SESSION";
	public static final long DEFAULT_SHELF_LIFE = 30000;
	
	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public void updateExpiration() {
		this.setExpiration(new Date(new Date().getTime() + DEFAULT_SHELF_LIFE));
		this.incrementVersion();
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
		this.incrementVersion();
	}

	public ServerSessionState(int seesionID, String[] location) {
		this.setSessionID(seesionID);
		this.setVersion(0);
		this.setLocationCount(location.length);
		this.setLocation(location);
		updateExpiration();
	};

	public void setClientSession(HttpServletResponse response) {
		response.addCookie(new Cookie(cookieName, super.generateCookie()));
	}
}
