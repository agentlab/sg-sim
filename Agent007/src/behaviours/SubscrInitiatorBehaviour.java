package behaviours;

import sg_sim.ControlAgent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class SubscrInitiatorBehaviour extends SubscriptionInitiator {
	private static final long serialVersionUID = 2596974171933664345L;

	public SubscrInitiatorBehaviour(ControlAgent myAgent, ACLMessage msg) {
		super(myAgent, msg);
	}

	@Override
	protected void handleAgree(ACLMessage agree) {
		System.out.println("Agent " + myAgent.getAID().getName() + " received the AGREE from agent " + agree.getSender().getName());
	}

	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Agent " + myAgent.getAID().getName() + " received the REFUSE from agent " + refuse.getSender().getName());
	}

	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("Agent " + myAgent.getAID().getName() + " received the INFORM from agent " + inform.getSender().getName());
	}
}