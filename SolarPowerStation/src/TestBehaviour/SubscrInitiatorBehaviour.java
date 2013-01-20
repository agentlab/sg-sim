package TestBehaviour;

import ontologies.AssignPowerRequest;
import ontologies.Message;
import sg_sim.SolarAgent;
import sg_sim.SubscrAgent;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.onto.basic.Action;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.proto.SubscriptionInitiator;

/**
 * This behaviour subscribes myAgent to the data of given agent. Then it
 * serves all subscription operation.
 */
public class SubscrInitiatorBehaviour extends SubscriptionInitiator {
	private static final long serialVersionUID = 2596974171933664345L;

	// for received subscription data
	protected String someYourData;

	/**
	 * Constructor - initiate subscription by sending msg
	 * 
	 * @param a
	 *            - the Agent
	 * @param msg
	 *            - message with SUBSCRIBE performative for initiate the
	 *            subscription
	 */
	public SubscrInitiatorBehaviour(SubscrAgent a, ACLMessage msg) {
		super(a, msg);
	}

	/**
	 * @see jade.proto.SubscriptionInitiator#handleAgree(jade.lang.acl.ACLMessage)
	 * 
	 * @param agree - message with AGREE performative
	 */
	@Override
	protected void handleAgree(ACLMessage agree) {
		System.out.println("Agent " + myAgent.getAID().getName() + " received the AGREE from agent " + agree.getSender().getName());
	}

	/**
	 * @see jade.proto.SubscriptionInitiator#handleRefuse(jade.lang.acl.ACLMessage)
	 * 
	 * @param refuse
	 *            - message with REFUSE performative
	 */
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Agent " + myAgent.getAID().getName() + " received the REFUSE from agent " + refuse.getSender().getName());
	}

	/**
	 * @see jade.proto.SubscriptionInitiator#handleInform(jade.lang.acl.ACLMessage)
	 * 
	 * @param inform
	 *            - message with INFORM performative
	 */
	@Override
	protected void handleInform(ACLMessage inform) {
		System.out.println("Agent " + myAgent.getAID().getName() + " received the INFORM from agent " + inform.getSender().getName());
		Concept action = null;
		ContentElement content;
		try {
			content = myAgent.getContentManager().extractContent(inform);
			action = ((Action)content).getAction();
			Message message_received = (Message) action;
		    boolean state_received = message_received.getState();
		    double current_power_received = message_received.getCurrentPower();
			double max_power_received = message_received.getMaxPower();
			System.out.println("state_received: " + state_received + "; current_power_received: " + current_power_received + "; max_power_received: " + max_power_received);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
