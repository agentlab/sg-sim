package ontologies;

import jade.content.*;

public class AssignTemperatureRequest implements AgentAction {
	private static final long serialVersionUID = 7057518253L;
	
	private double assigned_temperature;
	
	public double getAssignedTemperature() {
		return this.assigned_temperature;
	}
	
	public void setAssignedTemperature(double assigned_temperature) {
		this.assigned_temperature = assigned_temperature;
	}
}