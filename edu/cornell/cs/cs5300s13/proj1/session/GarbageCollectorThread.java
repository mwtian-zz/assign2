package edu.cornell.cs.cs5300s13.proj1.session;

public class GarbageCollectorThread implements Runnable {
	private SessionMap sessionMap;
	public int runCount;
	public static final long SLEEP_PERIOD = ServerSessionState.DEFAULT_SHELF_LIFE * 2;
	
	public GarbageCollectorThread(SessionMap sessionMap) {
		this.sessionMap = sessionMap;
		runCount = 0;
	}
	
	public void run() {
		while (true) {
			try {
				Thread.sleep(SLEEP_PERIOD);
			} catch (InterruptedException e) {
			}
			sessionMap.deleteExpired();
			runCount++;
		}
	}
}
