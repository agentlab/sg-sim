package behaviours;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

import java.util.Vector;

import agents.APSblockAgent;

import ontology.APSblockControlAction;



//Subscription initiator class
public class ControlCommandsSubscriptionInitiator extends SubscriptionInitiator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4425975760927249497L;
	public ControlCommandsSubscriptionInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		/**
		 * 
		 */
		System.out.println("Warning. Controller "+refuse.getSender().getName()+" refused subscription.");
	}
	@Override
	protected void handleInform(ACLMessage inform) {
		/**
		 * handle recieved notification message
		 */
		try {
			Action a=(Action)myAgent.getContentManager().extractContent(inform);
			if(a.getAction() instanceof APSblockControlAction){
				//process recieved result from the block
				APSblockControlAction ca=(APSblockControlAction)a.getAction();
				System.out.println(myAgent.getLocalName()+": control action has been recieved.");
				//do nothing, just print parameters
				System.out.println(myAgent.getLocalName()+": action type: "+ca.getActionType()+" Power: "+ca.getPower());
				((APSblockAgent)myAgent).setNextHourAction(ca);
				if(((APSblockAgent)myAgent).isFirstCommand()){
					((APSblockAgent)myAgent).setFirstCommand(false);
				}
			}
			
		} catch (UngroundedException e) {
			e.printStackTrace();
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
	}
	@Override
	protected void handleAllResponses(@SuppressWarnings("rawtypes") Vector responses){
		System.out.println(responses.size()+" responces have been recieved");
	}
	@Override
	public void cancel(AID reciever, boolean ignoreResponse){
		System.out.println("Subscription "+this.getClass()+" has been canceled by "+reciever.getName());
	}

}
