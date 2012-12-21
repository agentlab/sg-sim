package Ontologies;

import Ontologies.Message;
import jade.content.*;

public class SendMessage implements AgentAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private Message message;
	
	public Message getMessage() {
		return this.message;
	}
	
	public void setMessage(Message msg) {
		this.message = msg;
	}

}
