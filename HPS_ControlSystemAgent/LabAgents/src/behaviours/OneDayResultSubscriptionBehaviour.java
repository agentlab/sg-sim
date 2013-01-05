package behaviours;

import ontology.PowerProducerOntology;
import agents.PowerProducerAgent;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;

public class OneDayResultSubscriptionBehaviour extends OneShotBehaviour {

	private static final long serialVersionUID = 2292944312351926229L;
	private PowerProducerAgent producer;
	private AID responder;
	
	
	public OneDayResultSubscriptionBehaviour(PowerProducerAgent producer,
			AID responder) {
		super();
		this.producer = producer;
		this.responder = responder;
	}


	@Override
	public void action() {
		//prepare initiator and run it
		System.out.println(producer.getLocalName()+": subscribing to the HPS controller: "+this.responder);
		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(PowerProducerOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("one-day-result-subscription");
		subscribe.addReceiver(this.responder);
		//start subscription protocol
		producer.addBehaviour(new OneDayResultSubscriptionInitiator(this.producer,subscribe));
	}

}
