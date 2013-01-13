package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class CSAgent extends Agent {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 4865824138314558688L;
public String EValue;
	
	protected void setup() {
		// Printout a welcome message
		System.out.println("Control System Agent "+getAID().getName()+" is ready.");
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			EValue =  (String) args[0];
			System.out.println("CSAgent: Energy Value "+EValue);
		
			addBehaviour(new TickerBehaviour(this, 15000) {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 56315723152540972L;

				protected void onTick() {
					System.out.println("CSAgent: Sending value to BOAgent "+EValue);
					// Update the list of seller agents
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(new AID("BOAgent", AID.ISLOCALNAME));
					
					msg.setLanguage("English");
					msg.setContent(EValue);
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
				System.out.println(msg.getContent());
				if(msg.getContent()=="normal") {
					System.out.println("CSAgent: Work mode is normal");
				}
				else {
					System.out.println("CSAgent: Work mode is econom");
				}				
			}			
			else {
				block();
			}
		}
	}
}
