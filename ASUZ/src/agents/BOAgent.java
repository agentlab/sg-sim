package agents;

import java.util.Date;

import classes.Subscriber;
import jade.content.lang.sl.*;
import jade.core.Agent;
import jade.proto.SubscriptionResponder;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import onto.*;
import subscription.BOAgentSubscrManager;


/**
 * Conditioner class
 */
public class BOAgent extends Agent 
{
	private static final long serialVersionUID = 3731134492500110874L;
	//
	public String name = "BuildingOwner";		// Agent's name on yellow page service
	public String type = "BuildingOwner";		// Agent's type on yellow page service
	//
	private SubscriptionResponder dfSubscriptionResponder;
	private BOAgentSubscrManager subManager;
	//
	private double consumption = 20;
	
	@Override
	protected void setup() 
	{
		System.out.println(this.getAID().getName() + " started.");
		//
		this.registerOntology();
		//
		this.createSubscrManager();
		//
		System.out.println("Agent " + this.getAID().getName() + " is ready.");
	}
	
	@Override
	protected void takeDown() 
	{
		System.out.println("Agent " + this.getLocalName() + " is shutting down.");
	}
	

		
		

	
	public double getPowerConsumption()
	{
		return this.consumption;
	}
	
	private void createSubscrManager()
	{
		// Service registering on yellow page service
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.type); 
		sd.setName(this.name);
		//
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		dfd.addServices(sd);
		try 
		{
			DFService.register(this, dfd);
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}	
		// SubscriptionManager's initialization
		this.subManager = new BOAgentSubscrManager(this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
		this.dfSubscriptionResponder = new SubscriptionResponder(this, mt, this.subManager);
		this.addBehaviour(this.dfSubscriptionResponder);
		
	}
	
	private void registerOntology()
	{
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(SubscriptionMessageOntology.getInstance());
	}
	
}
