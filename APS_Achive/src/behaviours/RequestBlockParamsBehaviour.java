package behaviours;

import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREInitiator;
import jade.proto.SubscriptionResponder;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
//import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.Date;
import java.util.Vector;

import agents.APSblocksControlAgent;

import ontology.APSDescriptor;
import ontology.APSOntology;
import ontology.APSblockDescriptor;


public class RequestBlockParamsBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2462149039167225390L;
	private AID[] agentIDs;
	private int nResponders;
	private final int REPLY_TIMEOUT=10000;
	public RequestBlockParamsBehaviour(AID[] ids){
		super();
		agentIDs = ids;
		nResponders = agentIDs.length;
	}
	@Override
	public void action() {
		System.out.println(myAgent.getLocalName()+": requesting parameters to "+nResponders+" APSBlocks.");
		// Fill the REQUEST message
		ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
		for (int i = 0; i < nResponders; ++i) {
			msg.addReceiver(agentIDs[i]);
		}
		msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		msg.setLanguage(new SLCodec().getName());
		msg.setOntology(APSOntology.getInstance().getName());
		// We want to receive a reply in REPLY_TIMEOUT milliseconds
		msg.setReplyByDate(new Date(System.currentTimeMillis() + REPLY_TIMEOUT));
		msg.setContent("params-request");

		//request parameters from the agents
		myAgent.addBehaviour(new AchieveREInitiator(myAgent, msg) {
			private static final long serialVersionUID = 915141201502051605L;
			protected void handleInform(ACLMessage inform) {						
				//reading the inform message response
				try {
					//parameters have been received, saving
					Predicate pr = (Predicate)myAgent.getContentManager().extractContent(inform);
					if(pr instanceof APSblockDescriptor){
						((APSblocksControlAgent)myAgent).addBlock((APSblockDescriptor)pr);
						System.out.println(myAgent.getLocalName()+": agent "+inform.getSender().getName()+" has provided parameters");
						//adding the control commands subscription responder
						((APSblocksControlAgent)myAgent).setCcSubMngr(new ControlCommandsSubscriptionManager((APSblocksControlAgent)myAgent));
						MessageTemplate ccMT = MessageTemplate.and(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),MessageTemplate.MatchOntology(APSOntology.getInstance().getName()));
						SubscriptionResponder ccSubResponder = new SubscriptionResponder(myAgent, ccMT, ((APSblocksControlAgent)myAgent).getCcSubMngr());
						myAgent.addBehaviour(ccSubResponder);
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
				nResponders--;
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
				if (notifications.size() < nResponders) {
					// Some responder didn't reply within the specified timeout
					System.out.println("Timeout expired: missing "+(nResponders - notifications.size())+" responses");
				}
				else{
					double pow=0;
					for(APSblockDescriptor block:((APSblocksControlAgent)myAgent).getBlocks()){
						pow+=block.getMaxP();
					}
					((APSblocksControlAgent)myAgent).setMyParams(new APSDescriptor(myAgent.getAID(), pow));
					//TODO ((APSblocksControlAgent)myAgent).setMyParamsDone(true);
					ServiceDescription sd= new ServiceDescription();
					sd.setType("APS-generator-control");
					sd.setName("APSController");
					((APSblocksControlAgent)myAgent).dfd.addServices(sd);
					try {
						//DFService.register(myAgent, dfd);
						DFService.modify(myAgent, ((APSblocksControlAgent)myAgent).dfd);
						
					} catch (FIPAException e) {
						e.printStackTrace();
					}
				}
			}
		} );
	}

}
