package ontologies;

import jade.content.*;


public class SendMessage implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6876274843885978495L;
	private Electricity msg_el;
	private TimeDelay msg_td;
	private State msg_state;


	public Electricity getMsgElectro() {
		return this.msg_el;
	}

	public TimeDelay getMsgTD() {
		return this.msg_td;
	}

	public State getMsgState() {
		return this.msg_state;
	}

	public void setMsg(Electricity msg) {
		this.msg_el = msg;
	}
	public void setMsg(State msg) {
		this.msg_state = msg;
	}
	public void setMsg(TimeDelay msg) {
		this.msg_td = msg;
	}
}