package ontologies;

import jade.content.*;

public class BeginTemperatureTransitionRequest implements AgentAction {
	private static final long serialVersionUID = 7057518253L;
	
	private double assigned_temperature;
	private boolean assigned_temperature_is_valid;
	
	public double getAssignedTemperature() {
		return this.assigned_temperature;
	}
	
	public boolean getAssignedTemperatureIsValid() {
		return this.assigned_temperature_is_valid;
	}
	
	public void setAssignedTemperature(double assigned_temperature) {
		this.assigned_temperature = assigned_temperature;
	}
	
	public void setAssignedTemperatureValid(boolean val) {
		this.assigned_temperature_is_valid = val;
	}
}