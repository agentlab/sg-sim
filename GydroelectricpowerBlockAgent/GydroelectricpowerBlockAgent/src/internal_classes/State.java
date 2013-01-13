package internal_classes;

import jade.content.Concept;

public class State implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 21L;
	private double current_power;
	private double max_power;
	private boolean state;
	
	public double getCurrentPower() {
		return this.current_power;
	}
	
	public boolean getState() {
		return this.state;
	}
	
	public double getMaxPower() {
		return this.max_power;
	}
	
	public void setCurrentPower(double current_power) {
		this.current_power=current_power;
	}
	
	public void setMaxPower(double max_power) {
		this.max_power=max_power;
	}
	
	public void setState(boolean state) {
		this.state=state;
	}
}
