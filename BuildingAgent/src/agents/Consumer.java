package agents;

import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class Consumer extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6498299363528022095L;
	String volume;
	protected void setup(){
		System.out.println("Consumer "+getAID().getName()+" is ready.");
		
		this.getContentManager().registerLanguage(new SLCodec());					
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		
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
					msg.setLanguage(new SLCodec().getName());	
					msg.setOntology(RequestOntology.getInstance().getName());
					msg.setContent(volume);
					
					InformMessage imsg = new InformMessage();
					imsg.setVolume(Integer.parseInt(volume));
					try {
						((Consumer)myAgent).getContentManager().fillContent(msg, imsg);
					} catch (CodecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
