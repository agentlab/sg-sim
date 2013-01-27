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
	
}
