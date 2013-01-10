package behaviours;

import ontology.PowerProducerOntology;
import agents.HPSblocksControlAgent;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class PowerPlansSubscriptionBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5241679575983699569L;
	private HPSblocksControlAgent controller;
	private AID responder;
	
	public PowerPlansSubscriptionBehaviour(HPSblocksControlAgent controller,
			AID responder) {
		super();
		this.controller = controller;
		this.responder = responder;
	}

	@Override
	public void action() {
		//prepare initiator and run it
		System.out.println(controller.getLocalName()+": subscribing to the PowerProducer: "+this.responder);
		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(PowerProducerOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("power-plans-subscription");
		subscribe.addReceiver(responder);
		this.controller.addBehaviour(new PowerPlansSubscriptionInitiator(this.controller,subscribe));
	}

}
