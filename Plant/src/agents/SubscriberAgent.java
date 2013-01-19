package agents;

import onto.SubscriptionMessage.SubscriptionMessageType;
import onto.SubscriptionMessageOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
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
	//
	private Codec codec = new SLCodec();
	private Ontology ontology = SubscriptionMessageOntology.getInstance();
	//
	private SubscriptionMessageType subscrtype;

	/**
	 * Initialization
	 */
	@Override
	protected void setup() 
	{
		System.out.println(getAID().getName() + " started.");
		//
		this.subscrtype = SubscriptionMessageType.PowerConsumption;
		//
		getContentManager().registerLanguage(this.codec);
		getContentManager().registerOntology(this.ontology);
		
		this.subscribe();
		//
		System.out.println("Agent " + getAID().getName() + " is ready.");
	}
	
	/**
	 * Subscribing to the first finding service
	 */
	protected void subscribe() 
	{
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Plants");
		sd.setName("PlantAgent");
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
