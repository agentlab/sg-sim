package agents;

import jade.domain.DFService;
import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class ProducerAgent extends Agent {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -741223470022374171L;
	private AID[] brokerAgents;
	public int Evalue;
	private int price=100;
	private int volume;
	private ProducerAgent agent;
	public AID bestBuyer;
	public int bestPrice;
	//public int cnt=0;
	
	protected void setup() {
		// Printout a welcome message
		System.out.println("Building owner agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());					//registracia yazika
		this.getContentManager().registerOntology(RequestOntology.getInstance());	//registracia ontologii
		this.createResponder();
		
		// Dobavlenie povedenia
		addBehaviour(new OfferRequestsServer());			
	}
	
	//Povedenie dlya priema soobsheniy
		private class OfferRequestsServer extends CyclicBehaviour {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -355534649660658438L;
			private MessageTemplate mt;
			
			public void action() {
				mt = MessageTemplate.MatchSender(new AID("CSAgent",AID.ISLOCALNAME));
				ACLMessage msg = myAgent.receive(mt);
				if (msg != null) {
					// CFP Message received. Process it
					Evalue = Integer.parseInt(msg.getContent());
					System.out.println("BOAgent: Receive energy value "+Evalue);
					addBehaviour(new SendBehaviour());
				}
				else {
					block();
				}
			}	
		}

	private class SendBehaviour extends OneShotBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3363745877621540494L;
		int step=0;
		private MessageTemplate mt;		
		private int repliesCnt = 0;
		
		
		public void action(){
			mt=MessageTemplate.MatchConversationId("energy-selling");
			
			//Poisk Brokerov
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("energy-selling");
			template.addServices(sd);
			try {
				DFAgentDescription[] result = DFService.search(myAgent, template); 
				System.out.println("Found the following seller agents:");
				brokerAgents = new AID[result.length];
				for (int i = 0; i < result.length; ++i) {
					brokerAgents[i] = result[i].getName();
					System.out.println(brokerAgents[i].getName());
				}
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}				
			//Otpravka zaprosa ceni i ob'ema
				
					ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
					for (int i=0;i<brokerAgents.length;i++) {
						request.addReceiver(brokerAgents[i]);
					}
					request.setContent("get-price-and-volume");
					send(request);			
		}		
	}
	
	
	private void createResponder()	
	{
		MessageTemplate mtr = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new AchieveREResponder(this, mtr) {						  
			private static final long serialVersionUID = 99691474816159152L;
			private ProducerAgent agent;
			private int cnt=0;
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {				  
				  	try {
						InformMessage a =  (InformMessage)getContentManager().extractContent(request);										
						if (a instanceof InformMessage) {										
							price=a.getPrice();
							volume=a.getVolume();
							if (bestBuyer == null || (price > bestPrice)) {
								bestPrice = price;
								bestBuyer = request.getSender();
								
							}							
						}									
					} 
					catch (UngroundedException e) {					
						e.printStackTrace();
					} 
					catch (CodecException e) {									
						e.printStackTrace();
					} 
					catch (OntologyException e) {									
						e.printStackTrace();
					} 								    
					ACLMessage informDone = request.createReply();
					informDone.setPerformative(ACLMessage.INFORM);
					informDone.setContent("Values received");								   
					cnt++;
					if (cnt==brokerAgents.length) {
						addBehaviour(new SendCFP());
						cnt=0;
					}
					return informDone;					
			}							
		});
	}
	
	private class SendCFP extends OneShotBehaviour {
		
		private MessageTemplate mt;
		public void action() {
			//switch (step){
			//case 0:
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);			
				cfp.addReceiver(bestBuyer);			
				cfp.setContent("Buy-Request");				
				cfp.setReplyWith("cfp"+System.currentTimeMillis());
				cfp.setConversationId("energy-selling");
				myAgent.send(cfp);
				//mt = MessageTemplate.and(MessageTemplate.MatchConversationId("energy-selling"),
				//		MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				addBehaviour(new ReceiveCFPReply());
						
				
			
			}			
		//}
	}
	private class ReceiveCFPReply extends CyclicBehaviour{
		

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage reply=myAgent.receive();
			
			if (reply!=null) {
				ACLMessage mode=new ACLMessage(ACLMessage.INFORM);
				mode.addReceiver(new AID("CSAgent",AID.ISLOCALNAME));
				if (reply.getPerformative()==ACLMessage.PROPOSE) {
					mode.setContent("normal");
				}
				else {
					mode.setContent("econom");
				}
				myAgent.send(mode);
			}
			else {
				block();
			}
		}
	}
	
}
