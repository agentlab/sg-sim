package agents;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import behaviours.TransformatorFindCustomersBehaviour;
import classes.Subscriber;
import jade.content.lang.sl.*;
import jade.core.AID;
import jade.core.Agent;
import jade.proto.SubscriptionResponder;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import onto.*;
import subscription.TransformatorSubscrManager;

/**
 * 
 */
public class Transformator extends Agent 
{
	private static final long serialVersionUID = 3731134492500110874L;
	//
	public String name = "Transformator";		// Agent's name on yellow page service
	public String type = "Transformator";		// Agent's type on yellow page service
	//
	private SubscriptionResponder dfSubscriptionResponder;
	private TransformatorSubscrManager subManager;
	//
	private double power_consumption = 0.0;
	//
	private Map<AID, Double> customers_consumption = new HashMap<AID, Double>(); 
	
	@Override
	protected void setup() 
	{
		System.out.println(this.getAID().getName() + " started.");
		//
		this.registerOntology();
		//
		this.subscribeCustomers();
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
	
	protected void subscribeCustomers() 
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Customer");
		sd.setName("Customer");
		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);	
		addBehaviour(new TransformatorFindCustomersBehaviour(template, this, 10000));
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
		this.subManager = new TransformatorSubscrManager(this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
		this.dfSubscriptionResponder = new SubscriptionResponder(this, mt, this.subManager);
		this.addBehaviour(this.dfSubscriptionResponder);
	}
	
	private void registerOntology()
	{
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(SubscriptionMessageOntology.getInstance());
	}
		
	public SendSubscriptionMessage getNotifyMessage(Subscriber subscriber)
	{
		SubscriptionMessage msg = new SubscriptionMessage();
		msg.setSendDate(new Date()); 											// Sending date	
		msg.setType(subscriber.getType());
		switch (subscriber.getType())
		{		
			case PowerConsumption:
				msg.setValue(this.getPowerConsumption());
				break;
				
			default:
				break;
		}
		SendSubscriptionMessage smsg = new SendSubscriptionMessage();
		smsg.setMessage(msg);
		
		return smsg;
	}
	
	public void setCustomerConsumption(AID customer, double consumption)
	{
		this.customers_consumption.put(customer, consumption);
	}
	
	private double getPowerConsumption()
	{
		this.power_consumption = 0;
		for (Double elem : this.customers_consumption.values()) 
		{
			this.power_consumption += elem;
	    }
		
		return this.power_consumption;
	}
}
