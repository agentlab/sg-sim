package behaviours;

import java.util.Vector;

import ontology.OneHourResult;

import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class OneHourResultSubscriptionInitiator extends SubscriptionInitiator {
	
	private static final long serialVersionUID = 6069858712007418979L;

	/**
	 * Behavior for HPS block controller agent
	 */
	public OneHourResultSubscriptionInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		/**
		 * 
		 */
		System.out.println("Warning. Agent "+refuse.getSender().getName()+" refused subscription.");
	}
	@Override
	protected void handleInform(ACLMessage inform) {
		/**
		 * handle recieved notification message
		 */
		try {
			Predicate ce=(Predicate)myAgent.getContentManager().extractContent(inform);
			if(ce instanceof OneHourResult){
				//process recieved result from the block
				OneHourResult res=(OneHourResult)ce;
				System.out.println("Agent "+inform.getSender().getName()+" generated "+((OneHourResult)ce).getPower()+" at "+((OneHourResult)ce).getDate());
				System.out.println("Generated power by agent "+inform.getSender().getLocalName()+" is "+res.getPower());
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
