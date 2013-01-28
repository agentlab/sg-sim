package Behaviours;

import java.util.Vector;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class TestAgentInitiatorBehaviour extends AchieveREInitiator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TestAgentInitiatorBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
		// TODO Auto-generated constructor stub
	}

	
	protected void handleInform(ACLMessage inform) {
		System.out.println("ParkBespilot sended INFORM to " + inform.getSender().getName());
	}
	
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("ParkBespilot sended REFUSE to " + refuse.getSender().getName());
	}
	
	protected void handleFailure(ACLMessage failure) {
		if(failure.getSender().equals(myAgent.getDefaultDF())) {
			System.out.println("Failure delivering message");
		} else {
			System.out.println("ParkBespilot recieved FAILURE from " + failure.getSender().getName());
		}
	}
	
	protected void handleAllResultNotifications(Vector notifications) { 					
	}

		// TODO Auto-generated method stub
		
}
