package behaviours;

import ontology.HPSOntology;
import agents.HPSblockAgent;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class ControlCommandsSubscriptionBehaviour extends OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1346664631527250513L;
	private HPSblockAgent block;
	private AID responder;

	/**
	 * Behavior for HPS block agent
	 */
	
	public ControlCommandsSubscriptionBehaviour(HPSblockAgent block, AID responder) {
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
		subscribe.setOntology(HPSOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("control-commands-subscription");
		subscribe.addReceiver(responder);
		this.block.addBehaviour(new ControlCommandsSubscriptionInitiator(this.block,subscribe));
	}	
}	