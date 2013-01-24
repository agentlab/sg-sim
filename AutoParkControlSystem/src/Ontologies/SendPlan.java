package Ontologies;

import Ontologies.PlanMessage;
import jade.content.*;
import jade.core.AID;

public class SendPlan implements AgentAction {

	/**
	 * Ёмулирует отправку плана действий
	 */
	private static final long serialVersionUID = 1L;
    private PlanMessage message;
	
	public PlanMessage getMessage() {
		return this.message;
	}
	
	public void setMessage(PlanMessage msg) {
		this.message = msg;
	}

}