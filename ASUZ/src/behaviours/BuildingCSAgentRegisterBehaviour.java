package behaviours;

import java.util.Date;
import onto.SubscriptionMessage;
import onto.SendSubscriptionMessage;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import subscription.BuildingCSAgentSubscrInitiator;
import agents.BuildingCSAgent;

/**
 * Find service in yellow pages
 */
public class BuildingCSAgentRegisterBehaviour extends TickerBehaviour 
{
	private static final long serialVersionUID = 3384651319762649616L;
	//
	protected DFAgentDescription template;
	private BuildingCSAgent subscriber;
	//
	public BuildingCSAgentRegisterBehaviour(DFAgentDescription template, BuildingCSAgent a, long period)
	{
		super(a, period);
		this.template = template;
		this.subscriber = a;
	}

	/**
	 * Finding respondent
	 */
	@Override
	protected void onTick() 
	{
		DFAgentDescription[] result;
		try 
		{
			result = DFService.search(subscriber, template);
			if (result.length > 0) 
			{
				AID subscriptionAgent = result[0].getName();
				//
				ACLMessage subscribe = createRequestMessage(subscriber, subscriptionAgent);
				subscribe.setPerformative(ACLMessage.SUBSCRIBE);
				subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
				// 
				subscriber.addBehaviour(new BuildingCSAgentSubscrInitiator((BuildingCSAgent)subscriber, subscribe));
				//
				stop();		// Delete behavior
			}
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		} 
	}
	
	/**
	 * Request message
	 */
	protected ACLMessage createRequestMessage(BuildingCSAgent sender, AID receiver) 
	{
		Date date = new Date();
		date.setTime(date.getTime() + 300000); 							// 5 minutes 
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setSender(sender.getAID());
		request.addReceiver(receiver);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);	
		request.setLanguage(sender.getCodec().getName()); 
		request.setOntology(sender.getOntology().getName());
		request.setReplyWith("rw" + sender.getName() + System.currentTimeMillis());
		request.setConversationId("conv" + sender.getName() + System.currentTimeMillis());
		// Message context
		SubscriptionMessage msg = new SubscriptionMessage();
		msg.setType(this.subscriber.getSubscrType());
		msg.setSendDate(date); 											// Subscription time
		
		
		SendSubscriptionMessage smsg = new SendSubscriptionMessage();
		smsg.setMessage(msg);
		
		try 
		{
			this.subscriber.getContentManager().fillContent(request, smsg);
		} 
		catch (CodecException e) 
		{
			e.printStackTrace();
		} 
		catch (OntologyException e)
		{
			e.printStackTrace();
		}
		return request;
	}
}

