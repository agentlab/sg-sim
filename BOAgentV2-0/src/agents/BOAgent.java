package agents;

import java.util.Date;
import java.util.Enumeration;
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
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.ContractNetInitiator;
import jade.proto.SubscriptionInitiator;

public class BOAgent extends Agent {
/**
	 * 
	 */
	private static final long serialVersionUID = 5238728634416626059L;
private int nResponders;
private String volume;
private AID[] brokeragents;
private BOAgent a;
public int rcnt=0;
	protected void setup() {
  	
  	
		System.out.println("Building Owner Agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
  		addBehaviour(new ReceiveFromCSAgent(this));
		
  	
  		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(RequestOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("Volume");
		subscribe.addReceiver((new AID("CSAgent", AID.ISLOCALNAME)));
		this.addBehaviour(new BOAgentSubscrInit(this,subscribe));
  		
  		// Fill the CFP message
  		
  	
  }
	private class BOAgentSubscrInit extends SubscriptionInitiator{

		public BOAgentSubscrInit(Agent a, ACLMessage msg) {
			super(a, msg);
			// TODO Auto-generated constructor stub
		}


		/**
		 * 
		 */
		
		@Override
		protected void handleRefuse(ACLMessage refuse) {
			/**
			 * 
			 */
			System.out.println("Warning. Controller "+refuse.getSender().getName()+" refused subscription.");
		}
		@Override
		protected void handleInform(ACLMessage msg) {
			/**
			 * handle recieved notification message
			 */

			// TODO Auto-generated method stub
			//mt=MessageTemplate.and(MessageTemplate.MatchSender(new AID("RetailBroker",AID.ISLOCALNAME)),MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			
			if (msg!=null){
				
			}
			
					
		}
		
		
		
	
		
		
	}
	private class ReceiveFromCSAgent extends CyclicBehaviour {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -4475845021867741580L;
		private MessageTemplate mtCS;
		private BOAgent agent;
		
		//Конструктор
		public ReceiveFromCSAgent(BOAgent a) {
			this.agent=a;
		}
		
		@Override
		public void action() {
			mtCS=MessageTemplate.and(MessageTemplate.MatchSender(new AID("CSAgent", AID.ISLOCALNAME)),MessageTemplate.MatchConversationId("Evalue"));
			ACLMessage receivecs=myAgent.receive(mtCS);
			if (receivecs!=null){
				InformMessage a;
				try {
					a = (InformMessage)getContentManager().extractContent(receivecs);
					volume=String.valueOf(a.getVolume());
				} catch (UngroundedException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (CodecException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (OntologyException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				//Поиск брокеров на желтых страницах
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("energy-selling");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template); 
					System.out.println("Found the following seller agents:");
					brokeragents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						brokeragents[i] = result[i].getName();
						System.out.println(brokeragents[i].getName());
					}
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
				
				ACLMessage msg = new ACLMessage(ACLMessage.CFP);
		  		for (int i = 0; i < brokeragents.length; ++i) {
		  			msg.addReceiver(brokeragents[i]);
		  		}
					msg.setProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET);
					
					// We want to receive a reply in 10 secs
					msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
					
					msg.setConversationId("price-request");
					
					msg.setLanguage(new SLCodec().getName());	
					msg.setOntology(RequestOntology.getInstance().getName());
					InformMessage imsg = new InformMessage();
					imsg.setVolume(Integer.parseInt(volume));
					try {
						((BOAgent)myAgent).getContentManager().fillContent(msg, imsg);
					} catch (CodecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					addBehaviour(new ContractNetInitiator(this.agent, msg) {
						
						/**
						 * 
						 */
						private static final long serialVersionUID = 4290227958411834245L;

						protected void handlePropose(ACLMessage propose, Vector v) {
							System.out.println("Agent "+propose.getSender().getName()+" proposed "+propose.getContent());
						}
						
						protected void handleRefuse(ACLMessage refuse) {
							
							System.out.println("Agent "+refuse.getSender().getName()+" refused");
							rcnt++;
							if (rcnt==brokeragents.length){
								ACLMessage csmsg=new ACLMessage(ACLMessage.INFORM);
								csmsg.setContent("econom");
								csmsg.addReceiver(new AID("CSAgent", AID.ISLOCALNAME));
								send(csmsg);
								rcnt=0;
							}
						}
						
						protected void handleFailure(ACLMessage failure) {
							if (failure.getSender().equals(myAgent.getAMS())) {
								// FAILURE notification from the JADE runtime: the receiver
								// does not exist
								System.out.println("Responder does not exist");
								ACLMessage csmsg= new ACLMessage(ACLMessage.INFORM);
								csmsg.addReceiver(new AID("CSAgent", AID.ISLOCALNAME));
								csmsg.setContent("econom");
								send(csmsg);
							}
							else {
								System.out.println("Agent "+failure.getSender().getName()+" failed");
							}
							// Immediate failure --> we will not receive a response from this agent
							
						}
						
						protected void handleAllResponses(Vector responses, Vector acceptances) {
							if (responses.size() < brokeragents.length) {
								// Some responder didn't reply within the specified timeout
								System.out.println("Timeout expired: missing "+(nResponders - responses.size())+" responses");
							}
							// Evaluate proposals.
							int bestProposal = 100000;
							AID bestProposer = null;
							ACLMessage accept = null;
							Enumeration e = responses.elements();
							while (e.hasMoreElements()) {
								ACLMessage msg = (ACLMessage) e.nextElement();
								if (msg.getPerformative() == ACLMessage.PROPOSE) {
									ACLMessage reply = msg.createReply();
									reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
									acceptances.addElement(reply);
									try {
										InformMessage a =  (InformMessage)getContentManager().extractContent(msg);										
										int proposal=a.getPrice();
										if (proposal < bestProposal) {
											bestProposal = proposal;
											bestProposer = msg.getSender();
											accept = reply;
										}
									} 
									catch (UngroundedException e1) {					
										e1.printStackTrace();
									} 
									catch (CodecException e1) {									
										e1.printStackTrace();
									} 
									catch (OntologyException e1) {									
										e1.printStackTrace();
									}
									
									
								}
							}
							// Accept the proposal of the best proposer
							if (accept != null) {
								System.out.println("Accepting proposal "+bestProposal+" from responder "+bestProposer.getName());
								accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							}						
						}
						
						protected void handleInform(ACLMessage inform) {
							System.out.println("Agent "+inform.getSender().getName()+" successfully performed the requested action");
							//ACLMessage modemsg=new ACLMessage(ACLMessage.INFORM);
							//modemsg.addReceiver(new AID("CSAgent",AID.ISLOCALNAME));
							//modemsg.setContent("normal");
							//send(modemsg);
							
							ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
							request.setContent("normal");
							//request.setProtocol(FIPANames.InteractionProtocols.FIPA_REQUEST);
							request.addReceiver(new AID("CSAgent", AID.ISLOCALNAME)); 
							myAgent.addBehaviour( new AchieveREInitiator(myAgent, request) { 
							protected void handleInform(ACLMessage inform) { 
							System.out.println("Protocol finished. Rational Effect achieved. Received the following message: "+inform); 
							} 
							});
							
						}
					} );
			}
		}
		
	}
}
