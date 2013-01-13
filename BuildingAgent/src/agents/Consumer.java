package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Consumer extends Agent {
	String volume;
	protected void setup(){
		System.out.println("Consumer "+getAID().getName()+" is ready.");
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			volume =  (String) args[0];
			System.out.println(getAID().getName()+": Energy Value "+volume);
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("energy-consumer");
			sd.setName("EnergyConsumer");
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
			addBehaviour(new TickerBehaviour(this, 15000) {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 56315723152540972L;

				protected void onTick() {
					
					// Update the list of seller agents
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					msg.addReceiver(new AID("Building", AID.ISLOCALNAME));
					
					msg.setLanguage("English");
					msg.setContent(volume);
					send(msg);
				} 
			} );
		}
		else {
			// Make the agent terminate
			System.out.println("CSAgent: No energy value");
			doDelete();
		}
	}
	protected void takeDown() {
		// Deregister from the yellow pages
		try {
		DFService.deregister(this);
		}
		catch (FIPAException fe) {
		fe.printStackTrace();
		}
	}
}
