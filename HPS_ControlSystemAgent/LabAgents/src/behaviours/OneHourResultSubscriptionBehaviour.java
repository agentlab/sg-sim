package behaviours;

import ontology.HPSOntology;
import agents.HPSblocksControlAgent;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class OneHourResultSubscriptionBehaviour extends OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1346664631527250513L;
	private HPSblocksControlAgent controller;
	private AID responder;

	/**
	 * Behavior for HPS block controller agent
	 */
	
	public OneHourResultSubscriptionBehaviour(HPSblocksControlAgent controller, AID responder) {
		super();
		this.controller = controller;
		this.responder=responder;
	}
	
	@Override
	public void action() {
		//prepare initiator and run it
		System.out.println(controller.getLocalName()+": subscribing to the HPS block: "+this.responder);
		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(HPSOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("one-hour-result-subscription");
		subscribe.addReceiver(this.responder);
		//start subscription protocol
		controller.addBehaviour(new OneHourResultSubscriptionInitiator(this.controller,subscribe));
	}	
}	