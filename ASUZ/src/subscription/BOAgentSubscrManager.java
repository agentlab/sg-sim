package subscription;

import jade.content.ContentElement;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;
import jade.core.AID;
//import agents.AID;
import agents.BOAgent;
import classes.*;
import behaviours.BOAgentSendingBehaviour;
import java.util.Date;
import java.util.Vector;
import onto.*;
import onto.SubscriptionMessage.SubscriptionMessageType;

/**
 * Subscription responder services
 */
public class BOAgentSubscrManager implements SubscriptionManager 
{
	private Vector<Subscriber> subscribers = new Vector<Subscriber>(); 		// Subscriber list
	protected BOAgent owner;
	private BOAgentSendingBehaviour tick_behavior = new BOAgentSendingBehaviour(owner, 1000, this); 

	public BOAgentSubscrManager(BOAgent c) 
	{
		this.owner = c;
	}

	/**
	 * Subscriber register
	 */
	public boolean register(Subscription sub) throws RefuseException, NotUnderstoodException 
	{
		Date date = null;
		SubscriptionMessageType type = null;
		//
		try 
		{
			// Unpack message
			ContentElement ce = owner.getContentManager().extractContent(sub.getMessage());
			if (ce instanceof SendSubscriptionMessage)
			{
				SendSubscriptionMessage smsg = (SendSubscriptionMessage) ce;
				type = smsg.getMessage().getType();								// Subscription type
				date = smsg.getMessage().getSendDate();							// Last message date
			}
		} 
		catch (UngroundedException e) 
		{
			e.printStackTrace();
		} 
		catch (OntologyException e) 
		{
			e.printStackTrace();
		} 
		catch (jade.content.lang.Codec.CodecException e) 
		{
			e.printStackTrace();
		} 
		
		if (type != null)
		{
			subscribers.add(new Subscriber(sub, type, date));
			this.confirm(sub);
			System.out.println("Agent " + 
							   owner.getAID().getName() + 
							   " registered the agent " + 
							   sub.getMessage().getSender().getName());
			//
			if(subscribers.size() == 1)
			{
				owner.addBehaviour(tick_behavior); // At first subscriber add tick behavior
			}
		}

		return true; 
	}

	/**
	 * Delete subscriber
	 */
	public boolean deregister(Subscription sub) throws FailureException 
	{
		for (Subscriber subs : subscribers)
		{
			if (subs.getSub().equals(sub))
			{
				subscribers.remove(new Subscriber(subs.getSub(), subs.getType(), subs.getDate()));
			}
		}
		//
		System.out.println("Agent " + 
						   owner.getAID().getName() + 
						   " deregistered the agent " + 
						   sub.getMessage().getSender().getName());
		return false;
	}

	/**
	 * Tick action
	 */
	public void tick() 
	{
		if (this.subscribers.size() > 0)
		{
			for (Subscriber sub : this.subscribers) 
			{
				// Check subscription date
				Date now = new Date();
				if (sub.getDate().after(now))
				{
					this.notify(sub);
				}
				else
				{
					this.subscribers.remove(sub);
					break;
				}		
			}
		}
	}

	/**
	 * Notify the subscriber
	 */
	private void notify(Subscriber subscriber) 
	{
		try 
		{
			Subscription subscription = subscriber.getSub();
			ACLMessage notification = subscription.getMessage().createReply();
			notification.setPerformative(ACLMessage.INFORM); 					// New message 			
			// Create context		
			SendSubscriptionMessage smsg = this.owner.getNotifyMessage(subscriber);
			this.owner.getContentManager().fillContent(notification, smsg);
			subscription.notify(notification); 												// Send message		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	/**
	 * Agree to subscribe 
	 */
	private void confirm(Subscription sub) 
	{
		try 
		{
			ACLMessage notification = sub.getMessage().createReply();
			notification.setPerformative(ACLMessage.AGREE);
			sub.notify(notification);												// Send message
			//
			System.out.println("Agent " + 
							   owner.getAID().getName() + 
							   " sent AGREE message to the agent " + 
							   sub.getMessage().getSender().getName());
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
}