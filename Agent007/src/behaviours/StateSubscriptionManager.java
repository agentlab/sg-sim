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

import sg_sim.WindTurbineAgent;

/**
 * This class serves all subscription operation form subscription responder side
 * After first subscriber registration it starts DataProcessingBehaviour.
 * 
 * It's methods called by jade.proto.SubscriptionResponder behaviour.
 * 
 * @see jade.proto.SubscriptionResponder
 *
 */
public class StateSubscriptionManager implements SubscriptionManager {
	protected Vector<Subscription> subscriptions = new Vector<Subscription>();
	protected WindTurbineAgent windTurbineAgent;
	protected WindTurbineSubscriptionBehaviour windTurbineSubscriptionBehaviour;

	/**
	 * Constructor
	 * 
	 * @param agent
	 */
	public StateSubscriptionManager(WindTurbineAgent agent) {
		windTurbineAgent = agent;
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
		System.out.println("Agent " + windTurbineAgent.getAID().getName() + " registered the agent " + sub.getMessage().getSender().getName());

		if(subscriptions.size() == 1)
			windTurbineAgent.addBehaviour(new WindTurbineStateNotificationBehaviour(windTurbineAgent, this));
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

		System.out.println("Agent " + windTurbineAgent.getAID().getName() + " deregistered the agent " + sub.getMessage().getSender().getName());
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
			windTurbineAgent.getContentManager().registerOntology(FIPAManagementOntology.getInstance());
			Action act = new Action(windTurbineAgent.getAID(), sm);
			windTurbineAgent.getContentManager().fillContent(notification, act);

			sub.notify(notification);

			System.out.println("Agent " + windTurbineAgent.getAID().getName() + " notified the agent " + sub.getMessage().getSender().getName());
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

			System.out.println("Agent " + windTurbineAgent.getAID().getName() + " sent AGREE message to the agent " + sub.getMessage().getSender().getName());
		} catch (Exception e) {
			e.printStackTrace();
			// FIXME: Check whether a FAILURE message should be sent back.
		}
	}

	public void setWindTurbine(WindTurbineSubscriptionBehaviour wt) {
		this.windTurbineSubscriptionBehaviour = wt;
	}

	public WindTurbineSubscriptionBehaviour windTurbine() {
		return this.windTurbineSubscriptionBehaviour;
	}
}