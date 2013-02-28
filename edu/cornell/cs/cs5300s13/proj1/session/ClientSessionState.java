package edu.cornell.cs.cs5300s13.proj1.session;

import javax.servlet.http.Cookie;

public class ClientSessionState {
	private int sessionID;
	private int version;
	private int primaryLocation;
	private int secondaryLocation;
	
	public ClientSessionState() {
	}
	
	public ClientSessionState(String[] fields) {
		sessionID = Integer.parseInt(fields[0]);
		version = Integer.parseInt(fields[1]);
		primaryLocation = Integer.parseInt(fields[2]);
		secondaryLocation = Integer.parseInt(fields[3]);
	}
	
	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getPrimaryLocation() {
		return primaryLocation;
	}

	public void setPrimaryLocation(int primaryLocation) {
		this.primaryLocation = primaryLocation;
	}

	public int getSecondaryLocation() {
		return secondaryLocation;
	}

	public void setSecondaryLocation(int secondaryLocation) {
		this.secondaryLocation = secondaryLocation;
	}

	public int incrementVersion() {
		if (version < Integer.MAX_VALUE) {
			version++;
		} else {
			version = 0;
		}
		return version;
	}
	
	public String generateCookie() {
		StringBuilder cookie = new StringBuilder();
		cookie.append(sessionID);
		cookie.append("_");
		cookie.append(version);
		cookie.append("_");
		cookie.append(primaryLocation);
		cookie.append("_");
		cookie.append(secondaryLocation);
		return cookie.toString();
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
