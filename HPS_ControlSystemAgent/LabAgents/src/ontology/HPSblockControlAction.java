package ontology;

import jade.content.AgentAction;

public class HPSblockControlAction implements AgentAction {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 5892880655358501431L;
	public static enum ControlType{
		Action1,
		Action2
	}
	//
	private double power;
	private ControlType actionType;
	private int num;
	
	public HPSblockControlAction() {
		this.actionType=ControlType.Action1;
		this.power=0;
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

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}
}
