package agents;


import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;

import jade.core.AID;
import jade.core.Agent;

import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

public class CSAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369928645280712754L;
	public String EValue;
	public Subscription sub;
	//Основной метод агента
	protected void setup() {
		
		CSAgentSubManager RTManager= new CSAgentSubManager(this);
	  	MessageTemplate odrMT=MessageTemplate.and(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),MessageTemplate.MatchOntology(RequestOntology.getInstance().getName()));
	  	SubscriptionResponder odrSubResponder = new SubscriptionResponder(this, odrMT, RTManager);
	  	this.addBehaviour(odrSubResponder);
		
		
		
		
		System.out.println("Control System Agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		this.createResponder();
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			EValue =  (String) args[0];
			System.out.println("CSAgent: Energy Value "+EValue);
			//Поведение для отправки объема энергии владельцу здания
			
		}
		else {
			// Make the agent terminate
			System.out.println("CSAgent: No energy value");
			doDelete();
		}
		
		
		
		
		
	}
	
	private class CSAgentSubManager implements SubscriptionManager {
		private CSAgent agent;

		public CSAgentSubManager(CSAgent agent) {
			super();
			this.agent = agent;
		}

		@Override
		public boolean register(Subscription s) throws RefuseException,	NotUnderstoodException {
			boolean result=false;
			
			if(s.getMessage().getContent().equalsIgnoreCase("Volume")){
				agent.sub=s;
				//System.out.println(controller.getLocalName()+": received subscription for "+s.getMessage().getContent()+" from "+s.getMessage().getSender().getLocalName());

				//add behavior for sending one hour notifications
				agent.addBehaviour(new TickerBehaviour(agent,
						10000) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 3674681447400834233L;
					@Override
					protected void onTick() {

						System.out.println("CSAgent: Sending value to PlantOrganizationAgent "+EValue);
						// Update the list of seller agents
						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						msg.addReceiver(new AID("POAgent", AID.ISLOCALNAME));
						msg.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
						msg.setConversationId("Evalue");
						msg.setLanguage(new SLCodec().getName());	
						msg.setOntology(RequestOntology.getInstance().getName());
						InformMessage imsg = new InformMessage();
						imsg.setVolume(Integer.parseInt(EValue));
						try {
							((CSAgent)myAgent).getContentManager().fillContent(msg, imsg);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						send(msg);
						//addBehaviour(new Receiver());
					
						
						
					}
				});

				result=true;
				this.confirm(sub);
			}
			return result;
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

		@Override
		public boolean deregister(Subscription s) throws FailureException {
			// TODO Auto-generated method stub
			return false;
		}

	}
	
		
	private void createResponder() {
		MessageTemplate mtr = MessageTemplate.and(MessageTemplate.MatchSender(new AID("POAgent", AID.ISLOCALNAME)), MessageTemplate.MatchPerformative(ACLMessage.REQUEST));//AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new AchieveREResponder(this, mtr)
						  {
							private static final long serialVersionUID = 99691474816159152L;
							private Broker agent;
							protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
							   {
								System.out.println("Mode was receiver. Workmode is: " + request.getContent());	
								ACLMessage informDone = request.createReply(); 
								informDone.setPerformative(ACLMessage.INFORM); 
								informDone.setContent("inform done");
								return informDone;
							   }
						  });
	}
}
