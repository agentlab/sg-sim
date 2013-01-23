package agents;


import java.util.Random;
import java.util.Vector;


import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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

	private static final long serialVersionUID = -2369928645280712754L;
	public String EValue;
	public Subscription sub;
	private Vector<Subscription> subscribers = new Vector<Subscription>();

	protected void setup() {
		
		CSAgentSubManager RTManager= new CSAgentSubManager(this);
	  	MessageTemplate odrMT=MessageTemplate.and(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),MessageTemplate.MatchOntology(RequestOntology.getInstance().getName()));
	  	SubscriptionResponder odrSubResponder = new SubscriptionResponder(this, odrMT, RTManager);
	  	this.addBehaviour(odrSubResponder);

		System.out.println("Агент контрольной системы "+getAID().getLocalName()+" готов");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		this.createResponder();	
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
				subscribers.add(sub);
				agent.addBehaviour(new TickerBehaviour(agent,
						10000) {
				
					private static final long serialVersionUID = 3674681447400834233L;
					@Override
					protected void onTick() {
						Random generator = new Random();
						EValue = Integer.toString(generator.nextInt(190)+50);
					    
						System.out.println("_________"); 
						System.out.println("Контрольная система Посылает значение затраченной энергии "+EValue);

						ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
						msg.addReceiver(new AID("AgentPB", AID.ISLOCALNAME));
						msg.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
						msg.setConversationId("Evalue");
						msg.setLanguage(new SLCodec().getName());	
						msg.setOntology(RequestOntology.getInstance().getName());
						InformMessage imsg = new InformMessage();
						imsg.setVolume(Integer.parseInt(EValue));
						try {
							((CSAgent)myAgent).getContentManager().fillContent(msg, imsg);
						} catch (CodecException e) {

							e.printStackTrace();
						} catch (OntologyException e) {

							e.printStackTrace();
						}
						send(msg);
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
				sub.notify(notification);										
			
				System.out.println("Контрольная система посылает AGREE агенту " + 
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
		MessageTemplate mtr = MessageTemplate.and(MessageTemplate.MatchSender(new AID("AgentPB", AID.ISLOCALNAME)), MessageTemplate.MatchPerformative(ACLMessage.REQUEST));//AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new AchieveREResponder(this, mtr)
						  {
							private static final long serialVersionUID = 99691474816159152L;
							private Broker agent;
							protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response)
							   {

								ACLMessage informDone = request.createReply(); 
								informDone.setPerformative(ACLMessage.INFORM); 
								informDone.setContent("inform done");
								return informDone;
							   }
						  });
	}
}
