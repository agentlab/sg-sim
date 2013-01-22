package behaviours;

import ontologies.*;
import jade.core.Agent;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;

public class HolodilnikRequestResponderBehaviour extends AchieveREResponder {
	private static final long serialVersionUID = 1L;

	private HolodilnikBehaviour holodilnik;

	public HolodilnikRequestResponderBehaviour(HolodilnikBehaviour wt, Agent a, MessageTemplate mt) {
		super(a, mt);
		holodilnik = wt;
	}

	protected ACLMessage prepareResponse(ACLMessage msg) throws NotUnderstoodException, RefuseException {
		boolean agreed = false;
		ContentElement content;
		Concept action = null;
		System.out.println("Message Received");
		try {
			content = myAgent.getContentManager().extractContent(msg);
			action = ((Action)content).getAction();
			
			if(action instanceof AssignTemperatureRequest) {
				System.out.println("Received AssignTemperatureRequest");
				agreed = true;
			} else if (action instanceof BeginTemperatureTransitionRequest) {
				System.out.println("Received BeginTemperatureTransitionRequest");
				agreed = true;
			} else {
				System.out.println("Received AnotherAction, but will not perform it");
				agreed = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(agreed) {
			ACLMessage agree = msg.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			System.out.println("Sent AGREE");
			return agree;
		} else {
			throw new RefuseException("unknown operation");
		}
	}
	
	protected ACLMessage prepareResultNotification(ACLMessage msg, ACLMessage response) throws FailureException {
		boolean performed = false;
		ContentElement content;
		Concept action = null;

		System.out.println("pRN");

		try {
			content = myAgent.getContentManager().extractContent(msg);
			action = ((Action)content).getAction();
			
			if(action instanceof AssignTemperatureRequest) {
				AssignTemperatureRequest atr = (AssignTemperatureRequest) action;
				System.out.println("Performing AssignTemperatureRequest");
				System.out.println("Requester assigned temperature: " + atr.getAssignedTemperature());
				if(holodilnik.setAssignedTemperature(atr.getAssignedTemperature())) {
					System.out.println("Check passed, temperature assigned");
					performed = true;
				} else {
					System.out.println("Out of bounds, temperature not assigned");
					performed = false;
				}
			}
			
			if (action instanceof BeginTemperatureTransitionRequest) {
				BeginTemperatureTransitionRequest bttr = (BeginTemperatureTransitionRequest) action;
				boolean isOk = true;
				System.out.println("Performing BeginTemperatureTransitionRequest");
				if (bttr.getAssignedTemperatureIsValid()) {
					System.out.println("Requester assigned temperature: " + bttr.getAssignedTemperature());
					if(holodilnik.setAssignedTemperature(bttr.getAssignedTemperature())) {
						System.out.println("Check passed, temperature assigned");
					} else {
						System.out.println("Out of bounds, temperature not assigned");
						isOk = false;
					}
				}  else {
					System.out.println("Requester did not assigned new temperature");
				}
				
				if(isOk) {
					holodilnik.change_temperature();
				}
				
				performed = isOk;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(performed) {
			ACLMessage inform = msg.createReply();
			SendMessage sm = new SendMessage();
			System.out.println("Agent "+myAgent.getLocalName()+": Action successfully performed, time delay is " + holodilnik.time_delay());
			sm.setMsgTd(holodilnik.time_delay());
			try {
				myAgent.getContentManager().fillContent(inform, new Action(msg.getSender(), sm));
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			inform.setPerformative(ACLMessage.INFORM);
			return inform;
		} else {
			throw new FailureException("out of bounds");
		}
	}
}