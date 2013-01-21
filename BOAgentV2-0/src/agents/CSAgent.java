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
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class CSAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2369928645280712754L;
	public String EValue;
	//Основной метод агента
	protected void setup() {
		System.out.println("Control System Agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		this.createResponder();
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			EValue =  (String) args[0];
			System.out.println("CSAgent: Energy Value "+EValue);
			//Поведение для отправки объема энергии владельцу здания
			addBehaviour(new TickerBehaviour(this, 15000) {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 56315723152540972L;
				private CSAgent agent;
				protected void onTick() {
					System.out.println("CSAgent: Sending value to BOAgent "+EValue);
					// Update the list of seller agents
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(new AID("BOAgent", AID.ISLOCALNAME));
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
			} );
		}
		else {
			// Make the agent terminate
			System.out.println("CSAgent: No energy value");
			doDelete();
		}
	}
	
		
	private void createResponder() {
		MessageTemplate mtr = MessageTemplate.and(MessageTemplate.MatchSender(new AID("BOAgent", AID.ISLOCALNAME)), MessageTemplate.MatchPerformative(ACLMessage.REQUEST));//AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
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
