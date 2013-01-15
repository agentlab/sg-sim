package behaviours;
import ontologies.*;

import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

import java.util.Vector;

import Agents.GydroelectricpowerBlockAgent;


/**
 * This class serves all subscription operation form subscription responder side
 * After first subscriber registration it starts DataProcessingBehaviour.
 * 
 * It's methods called by jade.proto.SubscriptionResponder behaviour.
 * 
 * @see jade.proto.SubscriptionResponder
 *
 */
//Вектор - список подписчиков
public class StateSubscriptionManager implements SubscriptionManager {
	protected Vector<Subscription> subscriptions = new Vector<Subscription>();
	protected Agents.GydroelectricpowerBlockAgent myAgent;
	protected GydroelectricpowerBlockAgentBehaviour GydroelectricpowerBlock;

	/**
	 * Constructor
	 * 
	 * @param agent
	 */
	public StateSubscriptionManager(GydroelectricpowerBlockAgent agent) {
		myAgent = agent;
		System.out.println("Subscription manager: Herzlich willkommen");
	}

	/**
	 * register the subscription
	 * 
	 * @param sub
	 *            - subscription data
	 */
	public boolean register(Subscription sub) throws RefuseException, NotUnderstoodException {
		subscriptions.add(sub);
		
		confirm(sub);
		System.out.println(sub.getMessage().toString());
		System.out.println("Agent " + myAgent.getAID().getName() + " registered the agent " + sub.getMessage().getSender().getName());
		
		if(subscriptions.size() == 1)
			myAgent.addBehaviour(new GydroelectricpowerBlockAgentStateNotificationBehaviour(myAgent, this));
		return true;
	}

	/**
	 * deregister the subscription
	 * 
	 * @param sub
	 *            - subscription data
	 */
	public boolean deregister(Subscription sub) throws FailureException {
		subscriptions.remove(sub);
		
		System.out.println("Agent " + myAgent.getAID().getName() + " deregistered the agent " + sub.getMessage().getSender().getName());
		return false;
	}

	/**
	 * Handle registrations/de-registrations/modifications by notifying
	 * subscribed agents if necessary
	 * 
	 * @param someYourData
	 *            - data to send
	 */
	void handleChange(SendMessage msg, String type) {
		for (Subscription sub : subscriptions) {
			if(sub.getMessage().getConversationId().charAt(0) == '0' && type == "STATE") {
				notify(sub, msg);
			}

			if(sub.getMessage().getConversationId().charAt(0) == '1' && type == "ELECTRICITY") {
				notify(sub, msg);
			}
		}
	}

	/**
	 * Notifies subscriber with INFORM message
	 * 
	 * @param sub
	 *            - subscription data
	 * @param someYourData
	 *            - data to send
	 */
	private void notify(Subscription sub, SendMessage sm) {
		try {
			ACLMessage notification = sub.getMessage().createReply();
			notification.setPerformative(ACLMessage.INFORM);
			//myAgent.getContentManager().registerOntology(FIPAManagementOntology.getInstance());
			Action act = new Action(myAgent.getAID(), sm);
			myAgent.getContentManager().fillContent(notification, act);

			sub.notify(notification);

			System.out.println("Agent " + myAgent.getAID().getName() + " notified the agent " + sub.getMessage().getSender().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends to subscriber AGREE message
	 * 
	 * @param sub
	 *            - subscription data
	 */
	private void confirm(Subscription sub) {
		try {
			ACLMessage notification = sub.getMessage().createReply();
			notification.setPerformative(ACLMessage.AGREE);

			// pass to Subscription the message to send
			sub.notify(notification);
			
			System.out.println("Agent " + myAgent.getAID().getName() + " sent AGREE message to the agent " + sub.getMessage().getSender().getName());
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME: Check whether a FAILURE message should be sent back.
		}
	}
	
	public void setGydroelectricpowerBlock(GydroelectricpowerBlockAgentBehaviour wt) {
		this.GydroelectricpowerBlock = wt;
	}
	
	public GydroelectricpowerBlockAgentBehaviour GydroelectricpowerBlock() {
		return this.GydroelectricpowerBlock;
	}
}