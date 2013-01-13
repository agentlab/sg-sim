package agents;

import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;

public class Broker extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7914134039610895896L;
	private String price;
	private String volume;
	
	private Broker agent;
	
	protected void setup() {
		// Printout a welcome message
		System.out.println("Broker Agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			price =  (String) args[0];
			volume=  (String) args[1];
			
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("energy-selling");
			sd.setName("Energy-Broker");
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}			
		}
		else{
			System.out.println(getAID().getName()+" No available arguments");
			doDelete();
		}
		addBehaviour(new BOReply(this));
	}

	private class BOReply extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1845815847183794626L;
		private Broker agent;
		private MessageTemplate mt;
		
		
		public BOReply(Broker a) {
			// TODO Auto-generated constructor stub
			this.agent=a;
		}
		
		public void action() {
			mt=MessageTemplate.and(MessageTemplate.MatchSender(new AID("BOAgent",AID.ISLOCALNAME)),MessageTemplate.MatchContent("get-price-and-volume"));
			ACLMessage msg = myAgent.receive(mt);			
			if (msg != null ) {
				System.out.println("receive from BO: "+msg.getContent());
				addBehaviour(new SendMsg(this.agent));
				addBehaviour(new ReplyCFP());
			}			
			else {
				block();
			}
		}
	}
	//Ответ на запрос о покупке
	private class ReplyCFP extends CyclicBehaviour {
		private MessageTemplate mt;
		
		@Override
		public void action() {
			mt=MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				ACLMessage reply = msg.createReply();
				reply.setPerformative(ACLMessage.PROPOSE);
				reply.setContent(String.valueOf(price));
				myAgent.send(reply);
			}
			else {
				block();
			}
		}		
	}
	
	private class SendMsg extends OneShotBehaviour {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1049217622023441302L;
		private Broker agent;
		public SendMsg(Broker a) {
			// TODO Auto-generated constructor stub
			this.agent=a;
			
		}
		
		@Override
		public void action() {
			// TODO Auto-generated method stub
			
			ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
			request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			request.addReceiver(new AID("BOAgent",AID.ISLOCALNAME));
			request.setLanguage(new SLCodec().getName());	
			request.setOntology(RequestOntology.getInstance().getName());
			InformMessage imessage = new InformMessage();
			imessage.setPrice(Integer.parseInt(price));
			imessage.setVolume(Integer.parseInt(volume));
			try {
				this.agent.getContentManager().fillContent(request, imessage);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.agent.addBehaviour(new AchieveREInitiator(this.agent, request)
			{			
			/**
				 * 
				 */
				private static final long serialVersionUID = -8866775600403413061L;

				protected void handleInform(ACLMessage inform)
				{	
					
				}
			});
		}
	}
}
