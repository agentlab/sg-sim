package message;

import jade.content.AgentAction;

public class ControlAction implements AgentAction
{
	private static final long serialVersionUID = 5281179776722803352L;
	//
	private double needpower;

	private boolean need_charge;
	
	public double getNeedPower() {
		return needpower;
	}
	public void setNeedPower(double needpower) {
		this.needpower = needpower;
	}
	
	public boolean isNeed_charge() {
		return need_charge;
	}
	public void setNeed_charge(boolean need_charge) {
		this.need_charge = need_charge;
	}

}
