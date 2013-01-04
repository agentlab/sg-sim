package behaviours;

import java.util.Vector;

import ontology.HPSblockControlAction;
import ontology.OneHourResult;
import ontology.PowerPlan;
import agents.HPSblocksControlAgent;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class PowerPlansSubscriptionInitiator extends SubscriptionInitiator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6895980861429879598L;
	private HPSblocksControlAgent controller;

	public PowerPlansSubscriptionInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
		// TODO Auto-generated constructor stub
		this.controller=(HPSblocksControlAgent) a;
	}
	protected void handleRefuse(ACLMessage refuse) {
		/**
		 * 
		 */
		System.out.println("Warning. Controller "+refuse.getSender().getName()+" refused subscription.");
	}
	@Override
	protected void handleInform(ACLMessage inform) {
		/**
		 * handle recieved notification message (power plan)
		 */
		try {
			Predicate ce=(Predicate)myAgent.getContentManager().extractContent(inform);
			if(ce instanceof PowerPlan){
				//process recieved result from the block
				PowerPlan nextDayPlan=(PowerPlan)ce;
				/**
				 * PROCESS NEXT DAY POWER PLAN
				 */
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
