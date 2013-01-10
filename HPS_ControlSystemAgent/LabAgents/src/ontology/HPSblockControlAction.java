package ontology;

import jade.content.AgentAction;

public class HPSblockControlAction implements AgentAction {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 5892880655358501431L;
	public static enum ControlType{
		Stop,
		Start,
		SetPower,
		NoAction
	}
	//
	private double power;
	private ControlType actionType;
	
	public HPSblockControlAction() {
		this.actionType=ControlType.Stop;
		this.power=0;
	}
	
	public HPSblockControlAction(double power, ControlType actionType) {
		this.power = power;
		this.actionType = actionType;
	}

	public ControlType getActionType() {
		return actionType;
	}

	public void setActionType(ControlType actionType) {
		this.actionType = actionType;
	}

	public double getPower() {
		return power;
	}

	public void setPower(double power) {
		this.power = power;
	}
}
