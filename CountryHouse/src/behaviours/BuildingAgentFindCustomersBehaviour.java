package behaviours;

import java.util.Date;
import java.util.Vector;
import onto.SendSubscriptionMessage;
import onto.SubscriptionMessage;
import onto.SubscriptionMessageOntology;
import subscription.BuildingAgentSubscrInitiator;
import agents.CountryHouse;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;

public class BuildingAgentFindCustomersBehaviour extends TickerBehaviour
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6053732627448916379L;
	private CountryHouse building;
	private DFAgentDescription template;
	private Vector<AID> customers = new Vector<AID>();

	public BuildingAgentFindCustomersBehaviour(DFAgentDescription template, CountryHouse a, long period)
	{
		super(a, period);
		this.building = a;
		this.template = template;
	}
	
	@Override
	public void onStart() 
	{
		System.out.println("Finding customers activated");
	}
	
	@Override
	protected void onTick() 
	{		
		DFAgentDescription[] result;
		try 
		{
			result = DFService.search(building, template);
			if (result.length > 0) 
			{
				for (int i = 0; i < result.length; i++)
				{
					AID c = result[i].getName();
					if (!this.customers.contains(c))
					{
						this.customers.add(c);
						//
						//
						ACLMessage subscribe = createRequestMessage(building, c);
						subscribe.setPerformative(ACLMessage.SUBSCRIBE);
						subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
						// 
						building.addBehaviour(new BuildingAgentSubscrInitiator((CountryHouse)building, subscribe));
						//
					}
				}
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
	protected ACLMessage createRequestMessage(CountryHouse sender, AID receiver) 
	{
		Date date = new Date();
		date.setTime(date.getTime() + 30000000);
		
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setSender(sender.getAID());
		request.addReceiver(receiver);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);	
		request.setLanguage(new SLCodec().getName()); 
		request.setOntology(SubscriptionMessageOntology.getInstance().getName());
		request.setReplyWith("rw" + sender.getName() + System.currentTimeMillis());
		request.setConversationId("conv" + sender.getName() + System.currentTimeMillis());
		// Message context
		SubscriptionMessage msg = new SubscriptionMessage();
		msg.setType(SubscriptionMessage.SubscriptionMessageType.PowerConsumption);
		msg.setSendDate(date); 											// Subscription time
		
		
		SendSubscriptionMessage smsg = new SendSubscriptionMessage();
		smsg.setMessage(msg);
		
		try 
		{
			this.building.getContentManager().fillContent(request, smsg);
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
