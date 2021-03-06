package behaviours;

import ontology.APSOntology;
import agents.APSblockAgent;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class ControlCommandsSubscriptionBehaviour extends OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7015450200626372368L;
	private APSblockAgent block;
	private AID responder;

	/**
	 * Behavior for APS block agent
	 */
	
	public ControlCommandsSubscriptionBehaviour(APSblockAgent block, AID responder) {
		super();
		this.block = block;
		this.responder=responder;
	}
	
	@Override
	public void action() {
		//prepare initiator and run it
		System.out.println(block.getLocalName()+": subscribing to the controller: "+this.responder);
		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(APSOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("control-commands-subscription");
		subscribe.addReceiver(responder);
		this.block.addBehaviour(new ControlCommandsSubscriptionInitiator(this.block,subscribe));
	}	
}	