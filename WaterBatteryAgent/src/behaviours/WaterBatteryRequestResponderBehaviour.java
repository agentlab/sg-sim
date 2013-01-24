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

public class WaterBatteryRequestResponderBehaviour extends AchieveREResponder {
	private static final long serialVersionUID = 1L;

	private WaterBatteryBehaviour waterbattery;

	public WaterBatteryRequestResponderBehaviour(WaterBatteryBehaviour wt, Agent a, MessageTemplate mt) {
		super(a, mt);
		waterbattery = wt;
	}

	protected ACLMessage prepareResponse(ACLMessage msg) throws NotUnderstoodException, RefuseException {
		boolean agreed = false;
		ContentElement content;
		Concept action = null;
		System.out.println("Message Received");
		try {
			content = myAgent.getContentManager().extractContent(msg);
			action = ((Action)content).getAction();
			
			if(action instanceof AssignPowerRequest) {
				System.out.println("Received AssignPowerRequest");
				agreed = true;
			} else if (action instanceof BeginWaterTransitionRequest) {
				System.out.println("Received BeginWaterTransitionRequest");
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
			
			if(action instanceof AssignPowerRequest) {
				AssignPowerRequest apr = (AssignPowerRequest) action;
				System.out.println("Performing AssignPowerRequest");
				System.out.println("Requester assigned power: " + apr.getAssignedPower());
				if(waterbattery.setAssignedPower(apr.getAssignedPower())) {
					System.out.println("Check passed, power assigned");
					performed = true;
				} else {
					System.out.println("Out of bounds, power not assigned");
					performed = false;
				}
			}
			
			if (action instanceof BeginWaterTransitionRequest) {
				BeginWaterTransitionRequest bptr = (BeginWaterTransitionRequest) action;
				boolean isOk = true;
				System.out.println("Performing BeginWaterTransitionRequest");
				if (bptr.getAssignedPowerIsValid()) {
					System.out.println("Requester assigned power: " + bptr.getAssignedPower());
					if(waterbattery.setAssignedPower(bptr.getAssignedPower())) {
						System.out.println("Check passed, power assigned");
					} else {
						System.out.println("Out of bounds, power not assigned");
						isOk = false;
					}
				}  else {
					System.out.println("Requester did not assigned new power");
				}
				
				if(isOk) {
					waterbattery.change_power();
				}
				
				performed = isOk;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(performed) {
			ACLMessage inform = msg.createReply();
			SendMessage sm = new SendMessage();
			TimeDelay td = new TimeDelay();
			td.setTime(waterbattery.time_delay());
			System.out.println("Agent "+myAgent.getLocalName()+": Action successfully performed, time delay is " + waterbattery.time_delay());
			sm.setMsgTd(waterbattery.time_delay());
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