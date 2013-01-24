/*
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

public class ParkBespilotnikovAgent extends Agent {

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

		
		@Override
		protected void handleRefuse(ACLMessage refuse) {

			System.out.println("Warning. Controller "+refuse.getSender().getName()+" refused subscription.");
		}
		@Override
		protected void handleInform(ACLMessage msg) {

			// TODO Auto-generated method stub
			//mt=MessageTemplate.and(MessageTemplate.MatchSender(new AID("RetailBroker",AID.ISLOCALNAME)),MessageTemplate.MatchPerformative(ACLMessage.INFORM));
			
			if (msg!=null){
				
			}
			
					
		}
		
		
		
	
		
		
	}
	private class ReceiveFromCSAgent extends CyclicBehaviour {
		

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
						System.out.println("Найдены следующие брокеры:");
						brokeragents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							brokeragents[i] = result[i].getName();
							System.out.println(brokeragents[i].getLocalName());
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
						
						//хотим получить ответ в течение 2 сек
						msg.setReplyByDate(new Date(System.currentTimeMillis() + 2000));
						
						msg.setConversationId("price-request");
						
						msg.setLanguage(new SLCodec().getName());	
						msg.setOntology(RequestOntology.getInstance().getName());
						InformMessage imsg = new InformMessage();
						
						imsg.setVolume(Integer.parseInt(volume));
						try {
							((ParkBespilotnikov)myAgent).getContentManager().fillContent(msg, imsg);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						addBehaviour(new ContractNetInitiator(this.agent, msg) {

							private static final long serialVersionUID = 4290227958411834245L;

							protected void handlePropose(ACLMessage propose, Vector v) {
								System.out.println("Агент "+propose.getSender().getLocalName()+" предлагает "+propose.getContent());
							}
							
							protected void handleRefuse(ACLMessage refuse) {
								
								System.out.println("Агент "+refuse.getSender().getLocalName()+" не прислал предложение");
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
									System.out.println("Ответчик не обнаружен");
									ACLMessage csmsg= new ACLMessage(ACLMessage.INFORM);
									csmsg.addReceiver(new AID("CSAgent", AID.ISLOCALNAME));
									csmsg.setContent("econom");
									send(csmsg);
								}
								else {
									System.out.println("Агент "+failure.getSender().getName()+" зафейлился");
								}
								// Immediate failure --> we will not receive a response from this agent
								
							}
							
							protected void handleAllResponses(Vector responses, Vector acceptances) {
								if (responses.size() < brokeragents.length) {
									// Some responder didn't reply within the specified timeout
									System.out.println("Таймаут превышен  "+(nResponders - responses.size()));
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
											//	System.out.println("Лучшим брокером признан " + bestProposer);
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
									System.out.println("Принимаем предложение по цене "+bestProposal+" от брокера "+bestProposer.getLocalName());
									accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
								}						
							}
							
							protected void handleInform(ACLMessage inform) {
								System.out.println("Агент "+inform.getSender().getLocalName()+" успешно продал парку энергию");
								ACLMessage modemsg=new ACLMessage(ACLMessage.INFORM);
								modemsg.addReceiver(new AID("CSAgent",AID.ISLOCALNAME));
								modemsg.setContent("normal");
								send(modemsg);
							}
						} );
				}
			}
			
		}
		}
	}
*/
	

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

public class ParkBespilotnikovAgent extends Agent {

	private static final long serialVersionUID = 5238728634416626059L;
private int nResponders;
private String volume;
private AID[] brokeragents;
public int rcnt=0;
	protected void setup() {

		System.out.println("Парк беспилотников "+getAID().getLocalName()+" готов.");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
  		addBehaviour(new ReceiveFromCSAgent(this));
		  	
  		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(RequestOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("Volume");
		subscribe.addReceiver((new AID("CSAgent", AID.ISLOCALNAME)));
		this.addBehaviour(new PBAgentSubscrInit(this,subscribe));
  	
  }
	private class PBAgentSubscrInit extends SubscriptionInitiator{

		private static final long serialVersionUID = 1L;
		public PBAgentSubscrInit(Agent a, ACLMessage msg) {
			super(a, msg);
			// TODO Auto-generated constructor stub
		}
	
		@Override
		protected void handleRefuse(ACLMessage refuse) {

			System.out.println(refuse.getSender().getLocalName()+" отменил подписку.");
		}
		@Override
		protected void handleInform(ACLMessage msg) {

			if (msg!=null){
				
			}			
		}
		
	}
	private class ReceiveFromCSAgent extends CyclicBehaviour {
		
		private static final long serialVersionUID = -4475845021867741580L;
		private MessageTemplate mtCS;
		private ParkBespilotnikovAgent agent;
		
		//Конструктор
		public ReceiveFromCSAgent(ParkBespilotnikovAgent a) {
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
					System.out.println("Парк беспилотников получил значение энергии: "+volume);
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
					System.out.println("Найдены торговые агенты:");
					brokeragents = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						brokeragents[i] = result[i].getName();
						System.out.println(brokeragents[i].getLocalName());
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
					
					//хотим получить ответ в течение 2 сек
					msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
					
					msg.setConversationId("price-request");
					
					msg.setLanguage(new SLCodec().getName());	
					msg.setOntology(RequestOntology.getInstance().getName());
					InformMessage imsg = new InformMessage();
					
					imsg.setVolume(Integer.parseInt(volume));
					try {
						((ParkBespilotnikovAgent)myAgent).getContentManager().fillContent(msg, imsg);
					} catch (CodecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					addBehaviour(new ContractNetInitiator(this.agent, msg) {

						private static final long serialVersionUID = 4290227958411834245L;

						protected void handlePropose(ACLMessage propose, Vector v) {
							System.out.println("Агент "+propose.getSender().getLocalName()+" предлагает "+propose.getContent());
						}
						
						protected void handleRefuse(ACLMessage refuse) {
							
							System.out.println("Парк не получил предложения от  "+refuse.getSender().getLocalName());
							
						}
						
						protected void handleFailure(ACLMessage failure) {
							if (failure.getSender().equals(myAgent.getAMS())) {

								System.out.println("Ответчик не обнаружен");
							}
							else {
								System.out.println("Агент "+failure.getSender().getName()+" зафейлился");
							}

						}
						
						protected void handleAllResponses(Vector responses, Vector acceptances) {
							if (responses.size() < brokeragents.length) {

								System.out.println("Таймаут превышен  "+(nResponders - responses.size()));
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
										//	System.out.println("Лучшим брокером признан " + bestProposer);
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

							if (accept != null) {
								System.out.println("Принимаем предложение по цене "+bestProposal+" от брокера "+bestProposer.getLocalName());
								accept.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							}						
						}
						
				
					} );
			}
		}
		
	}
}


