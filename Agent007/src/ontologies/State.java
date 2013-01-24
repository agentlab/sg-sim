package ontologies;

import jade.content.Concept;

public class State implements Concept {
	private static final long serialVersionUID = 21L;
	private int power;
	private int maxPower;

	public int getPower() {
		return power;
	}

	public int getMaxPower() {
		return maxPower;
	}

	public void setPower(int newPower) {
		power = newPower;
	}

	public void setMaxPower(int newMaxPower) {
		maxPower = newMaxPower;
	}
}