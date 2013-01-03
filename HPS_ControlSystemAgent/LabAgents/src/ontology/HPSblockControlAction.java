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
	private ControlType acttype;
	private int num;
	
	public HPSblockControlAction() {
		this.acttype=ControlType.Action1;
		this.power=0;
	}

	public ControlType getcType() {
		return acttype;
	}

	public void setcType(ControlType cType) {
		this.acttype = cType;
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
