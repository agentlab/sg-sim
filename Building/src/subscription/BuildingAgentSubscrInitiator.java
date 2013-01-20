package subscription;

import java.text.SimpleDateFormat;

import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import agents.BuildingAgent;

import onto.*;

/**
 * Subscriber initiator
 */
public class BuildingAgentSubscrInitiator extends SubscriptionInitiator 
{
	private static final long serialVersionUID = 2596974171933664345L;
	protected BuildingAgent subscriber;
	private String FORMAT = "yyyy-MM-dd hh:mm:ss";
	private SimpleDateFormat sdf = new SimpleDateFormat(FORMAT);

	public BuildingAgentSubscrInitiator(BuildingAgent a, ACLMessage msg) 
	{
		super(a, msg);
		this.subscriber = a;	
	}

	/**
	 * @see jade.proto.SubscriptionInitiator#handleAgree(jade.lang.acl.ACLMessage)
	 * 
	 * @param agree
	 *            - message with AGREE performative
	 */
	@Override
	protected void handleAgree(ACLMessage agree) 
	{
		System.out.println("Agent " + 
						   this.subscriber.getAID().getName() + 
						   " received the AGREE from agent " + 
						   agree.getSender().getName());
	}

	/**
	 * @see jade.proto.SubscriptionInitiator#handleRefuse(jade.lang.acl.ACLMessage)
	 * 
	 * @param refuse
	 *            - message with REFUSE performative
	 */
	@Override
	protected void handleRefuse(ACLMessage refuse) 
	{
		System.out.println("Agent " + 
						   this.subscriber.getAID().getName() + 
						   " received the REFUSE from agent " + 
						   refuse.getSender().getName());
	}

	/**
	 * @see jade.proto.SubscriptionInitiator#handleInform(jade.lang.acl.ACLMessage)
	 * 
	 * @param inform
	 *            - message with INFORM performative
	 */
	@Override
	protected void handleInform(ACLMessage inform) 
	{ 
		try 
		{
			ContentElement ce = subscriber.getContentManager().extractContent(inform);
			
			if (ce instanceof SendSubscriptionMessage)
			{	
				SendSubscriptionMessage smsg = (SendSubscriptionMessage) ce;
				System.out.println(this.subscriber.getAID().getName() + 
								   " Received data " + 
								   smsg.getMessage().getType().name() + " = " +
								   smsg.getMessage().getValue() + " " + 
								   sdf.format(smsg.getMessage().getSendDate()));
				//
				this.subscriber.setCustomerConsumption(inform.getSender(), smsg.getMessage().getValue());
			}
		
		} 
		catch (UngroundedException e) 
		{
			e.printStackTrace();
		} 
		catch (CodecException e) 
		{
			e.printStackTrace();
		} 
		catch (OntologyException e) 
		{
			e.printStackTrace();
		} 
	}

}
