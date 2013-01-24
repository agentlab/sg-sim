package ontologies;

import jade.content.Concept;

public class PowerRequest implements Concept {
	private static final long serialVersionUID = 1L;
	
	private int power;
	private int timeInterval;

	public PowerRequest() {
		power = 0;
		timeInterval = 0;
	}
	
	public PowerRequest(int newPower, int newTimeInterval) {
		power = newPower;
		timeInterval = newTimeInterval;
	}
	
	public int getPower() {
		return power;
	}
	
	public int getTimeInterval() {
		return timeInterval;
	}
	
	public void setPower(int newPower) {
		power = newPower;
	}
	
	public void setTimeInterval(int newTimeInterval) {
		timeInterval = newTimeInterval;
	}
}
