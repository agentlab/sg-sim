package Behaviours;

import Ontologies.ActionMessage;
import Ontologies.SendAction;
import Ontologies.SendPlan;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class TestAgentResponderBehaviour extends AchieveREResponder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestAgentResponderBehaviour(Agent a, MessageTemplate mt) {
		super(a, mt);
		// TODO Auto-generated constructor stub
	}

	protected ACLMessage prepareResponse(ACLMessage msg) throws NotUnderstoodException, RefuseException {
		boolean agreed = false;
		ContentElement content;
		Concept action = null;
		try {
			content = myAgent.getContentManager().extractContent(msg);
			action = ((Action)content).getAction();
			
			if(action instanceof SendAction) {
				System.out.println("WB Received SendAction! All right!");
				agreed = true;
			} else if (action instanceof SendPlan) {
				System.out.println("WB Received SendPlan! What are you doing? >(");
				agreed = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(agreed) {
			ACLMessage agree = msg.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
		} else {
			throw new RefuseException("unknown operation");
		}
	}
	
	protected ACLMessage prepareResultNotification(ACLMessage msg, ACLMessage response) throws FailureException {
		boolean performed = false;
		ContentElement content;
		Concept action = null;
		try {
			content = myAgent.getContentManager().extractContent(msg);
			action = ((Action)content).getAction();
			
			if(action instanceof SendAction) {
				System.out.println("Performing SendAction");
				this.performSendAction(msg);
				performed = true;
				//
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(performed) {
			System.out.println("Agent "+myAgent.getLocalName()+": Action successfully performed");
			ACLMessage inform = msg.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			return inform;
		} else {
			throw new FailureException("error");
		}
	}
	
	protected void performSendAction(ACLMessage msg) {
		//myAgent.addBehaviour(new AgentForWBRequestInitiatorBehaviour(myAgent, msg));
		ContentElement content;
		Concept concept = null;
		ActionMessage action;
		try {
			content = myAgent.getContentManager().extractContent(msg);
			concept = ((Action)content).getAction();
			SendAction SA = (SendAction) concept;
			action = SA.getMessage();
			System.out.println("Recieved action: "+ action.getAction());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	}
}
