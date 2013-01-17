package behaviours;

import ontology.TPPOntology;
import agents.TPPblocksControlAgent;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class OneHourResultSubscriptionBehaviour extends OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8025908530019935793L;
	private TPPblocksControlAgent controller;
	private AID responder;

	/**
	 * Behavior for TPP block controller agent
	 */
	
	public OneHourResultSubscriptionBehaviour(TPPblocksControlAgent controller, AID responder) {
		super();
		this.controller = controller;
		this.responder=responder;
	}
	
	@Override
	public void action() {
		//prepare initiator and run it
		System.out.println(controller.getLocalName()+": subscribing to the TPP block: "+this.responder);
		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(TPPOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("one-hour-result-subscription");
		subscribe.addReceiver(this.responder);
		//start subscription protocol
		controller.addBehaviour(new OneHourResultSubscriptionInitiator(this.controller,subscribe));
	}	
}	