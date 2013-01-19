package agents;

import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

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
					addBehaviour(new Receiver());
				} 
			} );
		}
		else {
			// Make the agent terminate
			System.out.println("CSAgent: No energy value");
			doDelete();
		}
	}
	
	//Поведение для принятия режима
	private class Receiver extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 75873930823995368L;

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage msg= myAgent.receive();
			if (msg!=null) {
				System.out.println("Mode was received, work mode is "+msg.getContent());
				
			}			
			else {
				block();
			}
		}
	}
}
