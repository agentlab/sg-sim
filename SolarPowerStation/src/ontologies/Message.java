package ontologies;

import jade.content.*;

public class Message implements AgentAction {

	private static final long serialVersionUID = 1L;

	private double current_power;
	private double max_power;
	private boolean state;
	private Electricity msg_el;
	private TimeDelay msg_td;
	
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
	
	public Electricity getMsgElectro() {
		return this.msg_el;
	}
	
	public TimeDelay getMsgTD() {
		return this.msg_td;
	}
	
	public void setMsg(Electricity msg) {
		this.msg_el = msg;
	}

	public void setMsg(TimeDelay msg) {
		this.msg_td = msg;
	}
}
