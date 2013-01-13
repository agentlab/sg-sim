package internal_classes;

import jade.content.*;

public class AssignPowerRequest implements AgentAction {
	private static final long serialVersionUID = 7057518253L;
	
	private double assigned_power;
	
	public double getAssignedPower() {
		return this.assigned_power;
	}
	
	public void setAssignedPower(double assigned_power) {
		this.assigned_power = assigned_power;
	}
}