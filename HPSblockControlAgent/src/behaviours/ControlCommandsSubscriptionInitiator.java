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

import ontology.HPSblockControlAction;



//Subscription initiator class
public class ControlCommandsSubscriptionInitiator extends SubscriptionInitiator {
	
	private static final long serialVersionUID = 6069858712007418979L;

	/**
	 * Behavior for HPS block agent
	 */
	public ControlCommandsSubscriptionInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
		// TODO Auto-generated constructor stub
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
			if(a.getAction() instanceof HPSblockControlAction){
				//process recieved result from the block
				HPSblockControlAction ca=(HPSblockControlAction)a.getAction();
				System.out.println("Control Action has been recieved by agent "+myAgent.getName());
				//do nothing, just print parameters
				System.out.println("Action type: "+ca.getcType());
				System.out.println("Power: "+ca.getPower());
			}
			
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
	@Override
	protected void handleAllResponses(Vector responses){
		System.out.println(responses.size()+" responces have been recieved");
	}
	@Override
	public void cancel(AID reciever, boolean ignoreResponse){
		System.out.println("Subscription "+this.getClass()+" has been canceled by "+reciever.getName());
	}

}
