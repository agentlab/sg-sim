package Behaviours;

import java.util.Vector;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class PBRequestInitiatorBehaviour extends AchieveREInitiator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PBRequestInitiatorBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
		// TODO Auto-generated constructor stub
	}
	
	protected void handleInform(ACLMessage inform) {
		System.out.println("CSParkBespAgent recieved INFORM from " + inform.getSender().getName());
	}
	
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("CSParkBespAgent recieved REFUSE from " + refuse.getSender().getName());
	}
	
	protected void handleFailure(ACLMessage failure) {
		if(failure.getSender().equals(myAgent.getDefaultDF())) {
			System.out.println("Failure delivering message");
		} else {
			System.out.println("CSParkBespAgent recieved FAILURE from " + failure.getSender().getName());
		}
	}
	
	protected void handleAllResultNotifications(Vector notifications) { 					
	}

		// TODO Auto-generated method stub
		
}