package ontologies;

import jade.content.*;

public class BeginWaterTransitionRequest implements AgentAction {
	private static final long serialVersionUID = 7057518253L;
	
	private double assigned_power;
	private boolean assigned_power_is_valid;
	
	public double getAssignedPower() {
		return this.assigned_power;
	}
	
	public boolean getAssignedPowerIsValid() {
		return this.assigned_power_is_valid;
	}
	
	public void setAssignedPower(double assigned_power) {
		this.assigned_power = assigned_power;
	}
	
	public void setAssignedPowerValid(boolean val) {
		this.assigned_power_is_valid = val;
	}
}