package Ontologies;

import Ontologies.PlanMessage;
import jade.content.*;

public class SendPlan implements AgentAction {


	private static final long serialVersionUID = 1L;
    private PlanMessage message;
	
	public PlanMessage getMessage() {
		return this.message;
	}
	
	public void setMessage(PlanMessage msg) {
		this.message = msg;
	}

}