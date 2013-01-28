package Ontologies;

import Ontologies.ActionMessage;
import jade.content.*;

public class SendAction implements AgentAction {

	private static final long serialVersionUID = 1L;
    private ActionMessage message;
	
	public ActionMessage getMessage() {
		return this.message;
	}
	
	public void setMessage(ActionMessage msg) {
		this.message = msg;
	}

}

