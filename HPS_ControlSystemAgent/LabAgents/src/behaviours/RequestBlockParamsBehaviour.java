package behaviours;

import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.domain.FIPANames;

import java.util.Date;
import java.util.Vector;

import agents.HPSblocksControlAgent;

import ontology.HPSOntology;
import ontology.HPSblockDescriptor;


public class RequestBlockParamsBehaviour extends OneShotBehaviour {

	/**
	 * Behavior for HPS block controller agent
	 */
	private static final long serialVersionUID = -8540417358948127692L;
	
	private AID[] agentIDs;
	private int nResponders;
	public RequestBlockParamsBehaviour(AID[] ids){
		super();
		agentIDs = ids;
		nResponders = agentIDs.length;
	}
	@Override
	public void action() {
		// TODO Auto-generated method stub
		System.out.println("Requesting parameters to "+nResponders+" hpsBlocks.");
	  	// Fill the REQUEST message
	  	ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
	  		for (int i = 0; i < nResponders; ++i) {
	  			msg.addReceiver(agentIDs[i]);
	  		}
				msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
				msg.setLanguage(new SLCodec().getName());
				msg.setOntology(HPSOntology.getInstance().getName());
				// We want to receive a reply in 10 secs
				msg.setReplyByDate(new Date(System.currentTimeMillis() + 10000));
				msg.setContent("params-request");
							
				//request parameters from the agents
				myAgent.addBehaviour(new AchieveREInitiator(myAgent, msg) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 915141201502051605L;
					protected void handleInform(ACLMessage inform) {						
							//reading the inform message response
							try {
								//parameters have been recieved, saving
								Predicate pr = (Predicate)myAgent.getContentManager().extractContent(inform);
								((HPSblocksControlAgent)myAgent).addBlock((HPSblockDescriptor)pr);
								System.out.println("Agent "+inform.getSender().getName()+" has provided parameters");
							} catch (UngroundedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CodecException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (OntologyException e) {
								// TODO Auto-generated catch block
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
					}
				} );
	}

}
