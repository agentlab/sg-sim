package ontologies;

import jade.content.Concept;

public class PowerChangeRequest implements Concept {
	private static final long serialVersionUID = 1L;
	
	private int power;

	public PowerChangeRequest() {
		power = 0;
	}
	
	public PowerChangeRequest(int newPower) {
		power = newPower;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int newPower) {
		power = newPower;
	}
}
