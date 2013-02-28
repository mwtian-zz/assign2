package edu.cornell.cs.cs5300s13.proj1.session;

import javax.servlet.http.Cookie;

public class ClientSessionState {
	private int sessionID;
	private int version;
	private int locationCount;
	private String[] location;
	
	public ClientSessionState() {
	}
	
	public ClientSessionState(String[] fields) {
		sessionID = Integer.parseInt(fields[0]);
		version = Integer.parseInt(fields[1]);
		locationCount = Integer.parseInt(fields[2]);
		location = new String[locationCount];
		for (int i = 0; i < locationCount; i++) {
			location[i] = fields[3 + i];
		}
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


	public int getLocationCount() {
		return locationCount;
	}

	public void setLocationCount(int locationCount) {
		this.locationCount = locationCount;
	}

	public String[] getLocation() {
		return location;
	}

	public void setLocation(String[] location) {
		this.location = location;
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
		cookie.append(locationCount);
		for (int i = 0; i < locationCount; i++) {
			cookie.append("_");
			cookie.append(location[i]);
		}
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
