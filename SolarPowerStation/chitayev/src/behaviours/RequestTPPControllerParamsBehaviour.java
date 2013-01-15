package behaviours;

import java.util.Date;
import java.util.Vector;

import ontology.TPPDescriptor;
import ontology.PowerProducerOntology;
import agents.PowerProducerAgent;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.SubscriptionResponder;

public class RequestTPPControllerParamsBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2602731019937928977L;
	private PowerProducerAgent producer;
	private final int REPLY_TIMEOUT=60000;

	public RequestTPPControllerParamsBehaviour(PowerProducerAgent producer) {
		super();
		this.producer = producer;
	}

	@Override
	public void action() {

		System.out.println(producer.getLocalName()+": requesting parameters from TPPcontroller "+producer.getControllerDescriptor().getTppId().getLocalName());
		// Fill the REQUEST message
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		msg.addReceiver(producer.getControllerDescriptor().getTppId());	
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(PowerProducerOntology.getInstance().getName());
		// We want to receive a reply in REPLY_TIMEOUT milliseconds
		msg.setReplyByDate(new Date(System.currentTimeMillis() + REPLY_TIMEOUT));
		msg.setContent("TPPcontroller-params-request");

		//request parameters from the agents
		producer.addBehaviour(new AchieveREInitiator(producer, msg) {
			private static final long serialVersionUID = 915141201502051605L;
			protected void handleInform(ACLMessage inform) {						
				//reading the inform message response
				try {
					//parameters have been received, saving
					Predicate pr = (Predicate)myAgent.getContentManager().extractContent(inform);
					if(pr instanceof TPPDescriptor){
						((PowerProducerAgent)myAgent).setControllerDescriptor((TPPDescriptor)pr);
						System.out.println(myAgent.getLocalName()+": agent "+inform.getSender().getLocalName()+" has provided parameters");
						//adding the power plans subscription responder
						((PowerProducerAgent)myAgent).setPpSubMngr(new PowerPlansSubscriptionManager((PowerProducerAgent) myAgent));
						MessageTemplate ppMT = MessageTemplate.and(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),MessageTemplate.MatchOntology(PowerProducerOntology.getInstance().getName()));
						SubscriptionResponder ppSubResponder = new SubscriptionResponder((PowerProducerAgent)myAgent, ppMT, ((PowerProducerAgent)myAgent).getPpSubMngr());
						myAgent.addBehaviour(ppSubResponder);
					}
				} catch (UngroundedException e) {
					e.printStackTrace();
				} catch (CodecException e) {
					e.printStackTrace();
				} catch (OntologyException e) {
					e.printStackTrace();
				}						
			}
			protected void handleRefuse(ACLMessage refuse) {
				System.out.println("Agent "+refuse.getSender().getName()+" refused to provide parameters");
			}
			protected void handleFailure(ACLMessage failure) {
				if (failure.getSender().equals(myAgent.getAMS())) {
					// FAILURE notification from the JADE runtime: the receiver
					// does not exist
					System.out.println("Responder "+failure.getSender().getName()+" does not exist");
				}
				else {
					System.out.println("Agent "+failure.getSender().getName()+" failed to provide parameters");
				}
			}
			@SuppressWarnings("rawtypes")
			protected void handleAllResultNotifications(Vector notifications) {
				if (notifications.size() == 0) {
					// Some responder didn't reply within the specified timeout
					System.out.println("Timeout expired: missing response from TPP controller");
				}
			}
		} );
	}

}
