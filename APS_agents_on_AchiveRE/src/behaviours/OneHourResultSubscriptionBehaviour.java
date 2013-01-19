package behaviours;

import ontology.APSOntology;
import agents.APSblocksControlAgent;
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
	private APSblocksControlAgent controller;
	private AID responder;

	/**
	 * Behavior for APS block controller agent
	 */
	
	public OneHourResultSubscriptionBehaviour(APSblocksControlAgent controller, AID responder) {
		super();
		this.controller = controller;
		this.responder=responder;
	}
	
	@Override
	public void action() {
		//prepare initiator and run it
		System.out.println(controller.getLocalName()+": subscribing to the APS block: "+this.responder);
		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(APSOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("one-hour-result-subscription");
		subscribe.addReceiver(this.responder);
		//start subscription protocol
		controller.addBehaviour(new OneHourResultSubscriptionInitiator(this.controller,subscribe));
	}	
}	