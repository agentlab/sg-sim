package behaviours;
import ontologies.*;

import jade.content.Concept;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.domain.FIPAAgentManagement.FIPAManagementOntology;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

import java.util.Vector;

import agents.SolarAgent;


import jade.domain.FIPANames;


/**
 * This class serves all subscription operation form subscription responder side
 * After first subscriber registration it starts DataProcessingBehaviour.
 * It's methods called by jade.proto.SubscriptionResponder behaviour.
 */

public class StateSubscriptionManager implements SubscriptionManager {
	protected Vector<Subscription> subscriptions = new Vector<Subscription>();
	protected agents.SolarAgent myAgent;
	protected WindTurbBehaviour windTurbine;

	/**
	 * Constructor
	 * @param agent
	 */
	public StateSubscriptionManager(SolarAgent agent) {
		myAgent = agent;
		System.out.println("Subscription manager: Herzlich willkommen");
	}

	/**
	 * register the subscription
	 * @param sub - subscription data
	 */
	public boolean register(Subscription sub) throws RefuseException, NotUnderstoodException {
		subscriptions.add(sub);
		
		confirm(sub);
		System.out.println(sub.getMessage().toString());
		System.out.println("Agent " + myAgent.getAID().getName() + " registered the agent " + sub.getMessage().getSender().getName());
		
		if(subscriptions.size() == 1)
			myAgent.addBehaviour(new WindTurbineStateNotificationBehaviour(myAgent, this));
		return true;
	}

	/**
	 * deregister the subscription
	 * @param sub- subscription data
	 */
	public boolean deregister(Subscription sub) throws FailureException {
		subscriptions.remove(sub);
		
		System.out.println("Agent " + myAgent.getAID().getName() + " deregistered the agent " + sub.getMessage().getSender().getName());
		return false;
	}

	/**
	 * Handle registrations/de-registrations/modifications by notifying
	 * subscribed agents if necessary
	 * @param someYourData- data to send
	 */
	void handleChange(Message msg, String type) {
		for (Subscription sub : subscriptions) {
			if(sub.getMessage().getConversationId().charAt(0) == '0' && type == "STATE") {
				notify(sub, msg);
			}

			if(sub.getMessage().getConversationId().charAt(0) == '1' && type == "ELECTRICITY") {
				notify(sub, msg);
			}
		}
	}

	private Codec codec = new SLCodec();
	private Ontology ontology = SolarAgentOntology.getInstance();
	
	/**
	 * Notifies subscriber with INFORM message
	 * @param sub- subscription data
	 * @param someYourData- data to send
	 */
	private void notify(Subscription sub, Message sm) {
		try {
			ACLMessage notification = sub.getMessage().createReply();
			myAgent.getContentManager().registerLanguage(codec); //регистрация языка и онтологии
			myAgent.getContentManager().registerOntology(ontology);
			notification.setPerformative(ACLMessage.INFORM);
			notification.setLanguage(codec.getName());
			notification.setOntology(ontology.getName());
			notification.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			try {
				myAgent.getContentManager().fillContent(notification, new Action(myAgent.getAID(), sm));
			} catch (Exception e) {
				e.printStackTrace();
			}
			sub.notify(notification);

			System.out.println("Agent " + myAgent.getAID().getName() + " notified the agent " + sub.getMessage().getSender().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Sends to subscriber AGREE message
	 * @param sub - subscription data
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
	
	public void setWindTurbine(WindTurbBehaviour wt) {
		this.windTurbine = wt;
	}
	
	public WindTurbBehaviour windTurbine() {
		return this.windTurbine;
	}
}