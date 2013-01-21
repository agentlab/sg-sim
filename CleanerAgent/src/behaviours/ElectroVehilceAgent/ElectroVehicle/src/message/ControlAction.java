package message;

import jade.content.AgentAction;

public class ControlAction implements AgentAction
{
	private static final long serialVersionUID = 5281179776722803352L;
	//
	private double xpath;
	private double ypath;
	private double zpath;
	private double speed;
	private boolean need_charge;
	
	public double getXpath() {
		return xpath;
	}
	public void setXpath(double xpath) {
		this.xpath = xpath;
	}
	public double getYpath() {
		return ypath;
	}
	public void setYpath(double ypath) {
		this.ypath = ypath;
	}
	public double getSpeed() {
		return speed;
	}
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public boolean isNeed_charge() {
		return need_charge;
	}
	public void setNeed_charge(boolean need_charge) {
		this.need_charge = need_charge;
	}
	public double getZpath() {
		return zpath;
	}
	public void setZpath(double zpath) {
		this.zpath = zpath;
	}
}
