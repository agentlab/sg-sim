package agents;

import onto.SubscriptionMessage.SubscriptionMessageType;
import onto.SubscriptionMessageOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import behaviours.RegisterBehaviour;

/**
 * Subscriber
 * Have two: RegisterBehaviour & StubscriptionInitiator 
 */
public class SubscriberAgent extends Agent 
{
	private static final long serialVersionUID = -2961241167339514488L;
	private SubscriptionMessageType subscrtype;
	//
	protected final String name = "SubscriberAgent"; 			// Agent's name on yellow page service
	protected final String type = "TestAgent"; 					// Agent's type on yellow page service
	//
	private Codec codec = new SLCodec();
	private Ontology ontology = SubscriptionMessageOntology.getInstance();
	//
	private String responder_name;

	/**
	 * Initialization
	 */
	@Override
	protected void setup() 
	{
		System.out.println(getAID().getName() + " started.");
		//
		this.parseArgs();
		//
		getContentManager().registerLanguage(this.codec);
		getContentManager().registerOntology(this.ontology);
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type);
		sd.setName(name);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try 
		{
			DFService.register(this, dfd);
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}
		this.subscribe();
		//
		System.out.println("Agent " + getAID().getName() + " is ready.");
	}
	
	private void parseArgs()
	{
		
		Object[] args = this.getArguments();
		if (args != null && args.length > 0)
		{
			String responder_type = (String)args[0];
			String subscr_type = (String)args[1];
			switch (Integer.valueOf(responder_type))
			{
				case 0:
					this.responder_name = "ConditionerAgent";
					
					switch (Integer.valueOf(subscr_type))
					{
						case 0:
							this.subscrtype = SubscriptionMessageType.PowerConsumption;
							break;
							
						case 1:
							this.subscrtype = SubscriptionMessageType.CoolingCapacity;
							break;
							
						default:
							this.subscrtype = SubscriptionMessageType.PowerConsumption;
							break;
					}
					
					break;
					
				case 1:
					this.responder_name = "CookerAgent";
					
					switch (Integer.valueOf(subscr_type))
					{
						case 0:
							this.subscrtype = SubscriptionMessageType.PowerConsumption;
							break;
							
						case 1:
							this.subscrtype = SubscriptionMessageType.Temperature;
							break;
							
						default:
							this.subscrtype = SubscriptionMessageType.PowerConsumption;
							break;
					}
					
					break;
					
				default:
					this.responder_name = "ConditionerAgent";
					this.subscrtype = SubscriptionMessageType.PowerConsumption;
					break;
			}
		}
		else
		{
			this.subscrtype = SubscriptionMessageType.PowerConsumption;
		}
	}

	/**
	 * Subscribing to the first finding service
	 */
	protected void subscribe() 
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType("ConsumerelEctronics");
		sd.setName(this.responder_name);
		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);	
		addBehaviour(new RegisterBehaviour(template, this, 1000));
	}

	@Override
	protected void takeDown() 
	{
		System.out.println("Agent " + getLocalName() + " is shutting down.");
	}
	
	public Codec getCodec()
	{
		return this.codec;
	}
	
	public Ontology getOntology()
	{
		return this.ontology;
	}
	
	public SubscriptionMessageType getSubscrType()
	{
		return this.subscrtype;
	}
}
