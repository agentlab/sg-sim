package agents;

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
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Building extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7773294349902276869L;
	
	protected Consumer agent;
	public AID[] consumers;
	
	protected void setup() {
		System.out.println("Building agent "+getAID().getName()+" is ready.");
		
		this.getContentManager().registerLanguage(new SLCodec());					
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		
		addBehaviour(new ReceiveBehaviour());
	}
		
	private class ReceiveBehaviour extends CyclicBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 5028217092792646964L;
		private MessageTemplate mt;
		private int msgCnt=0;
		private int volume;
		private int sumVol=0;
		@Override
		public void action() {
			
			ACLMessage msg= myAgent.receive();
			if (msg!=null) {
				
				mt=MessageTemplate.MatchConversationId("energy-consumer");
				
				//Searching consumers
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("energy-consumer");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template); 
					System.out.println("Found the following consumer agents:");
					consumers = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						consumers[i] = result[i].getName();
						System.out.println(consumers[i].getName());
					}
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
				try {
					InformMessage a =  (InformMessage)getContentManager().extractContent(msg);
					volume=a.getVolume();
					msgCnt=msgCnt+1;
					sumVol=sumVol+volume;
					if (msgCnt==consumers.length) {
						ACLMessage sum = new ACLMessage(ACLMessage.INFORM);
						sum.addReceiver(new AID("CSAgent", AID.ISLOCALNAME));
						sum.setLanguage(new SLCodec().getName());	
						sum.setOntology(RequestOntology.getInstance().getName());
						sum.setContent(String.valueOf(volume));
						InformMessage imsg = new InformMessage();
						imsg.setVolume(sumVol);
						try {
							((Building)myAgent).getContentManager().fillContent(sum, imsg);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						send(sum);
						msgCnt=0;
						sumVol=0;
					}
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else {
				block();
			}
		}
		
		
	}
}
