package agents;

import classes.Subscriber;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.*;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.proto.AchieveREResponder;
import jade.proto.SubscriptionResponder;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import onto.*;
import subscription.SubscrManager;

/**
 * Conditioner class
 */
public abstract class ApplianceAgent extends Agent 
{
	private static final long serialVersionUID = 3731134492500110874L;
	//
	public String name;		// Agent's name on yellow page service
	public String type;		// Agent's type on yellow page service
	//
	private SubscriptionResponder dfSubscriptionResponder;
	private SubscrManager subManager;
	
	@Override
	protected void setup() 
	{
		System.out.println(this.getAID().getName() + " started.");
		//
		this.registerOntology();
		//
		this.createResponder();
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
		this.subManager = new SubscrManager(this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
		this.dfSubscriptionResponder = new SubscriptionResponder(this, mt, this.subManager);
		this.addBehaviour(this.dfSubscriptionResponder);
		//
		System.out.println("Agent " + this.getAID().getName() + " is ready.");
	}
	
	@Override
	protected void takeDown() 
	{
		System.out.println("Agent " + this.getLocalName() + " is shutting down.");
	}
	
	private void registerOntology()
	{
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(ControlActionOntology.getInstance());
		this.getContentManager().registerOntology(SubscriptionMessageOntology.getInstance());
	}
		
	public abstract SendSubscriptionMessage getNotifyMessage(Subscriber subscriber);
	
	public abstract void controlAppliance(ControlAction ca);
	
	private void createResponder()
	{
		MessageTemplate mtr = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new AchieveREResponder(this, mtr)
						  {
							private static final long serialVersionUID = 99691474816159152L;

							protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
							   {
								   try 
									{
										Action a = (Action) getContentManager().extractContent(request);
										
										if (a instanceof Action)
										{	
											ControlAction ca = (ControlAction) a.getAction();	
											controlAppliance(ca);
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
								    //
								    ACLMessage informDone = request.createReply();
								    informDone.setPerformative(ACLMessage.INFORM);
								    informDone.setContent("inform done");
								    return informDone;
							   }
						  });
	}
}
