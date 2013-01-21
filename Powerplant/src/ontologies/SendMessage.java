package ontologies;

import jade.content.*;


public class SendMessage implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6876274843885978495L;
	private Electricity msgEl;
	private double msgTd;
	private State msgState;
	
	
	public Electricity getMsgEl() {
		return msgEl;
	}
	public void setMsgEl(Electricity msgEl) {
		this.msgEl = msgEl;
	}
	public double getMsgTd() {
		return msgTd;
	}
	public void setMsgTd(double msgTd) {
		this.msgTd = msgTd;
	}
	public State getMsgState() {
		return msgState;
	}
	public void setMsgState(State msgState) {
		this.msgState = msgState;
	}
	
	
	/*public Electricity getMsgElectro() {
		return this.msgEl;
	}
	
	public double getMsgTD() {
		return this.msgTd;
	}
	
	public State getMsgState() {
		return this.msgState;
	}
	
	public void setMsg(Electricity msg) {
		this.msgEl = msg;
	}
	public void setMsg(State msg) {
		this.msgState = msg;
	}
	public void setMsg(TimeDelay msg) {
		this.msgTd = msg.getTimeDelay();
	}*/
}
