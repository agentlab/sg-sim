package agents;


import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

public class RetailBrokerAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 454646934716181911L;
	private Subscription sub;
	protected RetailBrokerAgent rT;
	public int volume;
	public String name = "RetailBrokerAgent";		// Agent's name on yellow page service
	public String type = "RTA";		// Agent's type on yellow page service
	protected void setup(){	
		System.out.println("Retail Broker Agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		this.yellowPagesRegister();
		//adding one day result subscription responder
		RetailAgnetSubManager RTManager= new RetailAgnetSubManager(this);
	  	MessageTemplate odrMT=MessageTemplate.and(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),MessageTemplate.MatchOntology(RequestOntology.getInstance().getName()));
	  	SubscriptionResponder odrSubResponder = new SubscriptionResponder(this, odrMT, RTManager);
	  	this.addBehaviour(odrSubResponder);
	}
	
	private void yellowPagesRegister() {
		// TODO Auto-generated method stub
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
		
	}
	private class RetailAgnetSubManager implements SubscriptionManager {
		private RetailBrokerAgent agent;

		public RetailAgnetSubManager(RetailBrokerAgent agent) {
			super();
			this.agent = agent;
		}

		@Override
		public boolean register(Subscription s) throws RefuseException,
		NotUnderstoodException {
			boolean result=false;
			if(s.getMessage().getContent().equalsIgnoreCase("Value")){
				agent.sub=s;
				System.out.println(agent.getLocalName()+": received subscription for "+s.getMessage().getContent()+" from "+s.getMessage().getSender().getLocalName());
				//add behavior for sending one hour notifications
				agent.addBehaviour(new TickerBehaviour(agent,
						1000) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 3674681447400834233L;
					@Override
					protected void onTick() {
						volume =300;
						ACLMessage sndVolume = sub.getMessage().createReply();
						sndVolume.setPerformative(ACLMessage.INFORM);
						sndVolume.setLanguage(new SLCodec().getName());	
						sndVolume.setOntology(RequestOntology.getInstance().getName());
						InformMessage imessage = new InformMessage();
						imessage.setVolume(volume);
						try {
							((RetailBrokerAgent)myAgent).getContentManager().fillContent(sndVolume, imessage);
							sub.notify(sndVolume);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});

				result=true;
				this.confirm(sub);
			}
			return result;
		}

		@Override
		public boolean deregister(Subscription s) throws FailureException {
			// TODO Auto-generated method stub
			return false;
		}
		
		
		private void confirm(Subscription sub) 
		{
			try 
			{
				ACLMessage notification = sub.getMessage().createReply();
				notification.setPerformative(ACLMessage.AGREE);
				sub.notify(notification);												// Send message
			
				System.out.println("Agent sent AGREE message to the agent " + 
								   sub.getMessage().getSender().getName());
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}

	}
}
