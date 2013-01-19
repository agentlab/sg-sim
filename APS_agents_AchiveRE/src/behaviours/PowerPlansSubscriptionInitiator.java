package behaviours;

import java.util.Vector;

import ontology.PowerPlan;
import agents.APSblocksControlAgent;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class PowerPlansSubscriptionInitiator extends SubscriptionInitiator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8527701919071780822L;
	private APSblocksControlAgent controller;

	public PowerPlansSubscriptionInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
		this.controller=(APSblocksControlAgent) a;
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
			Predicate ce=(Predicate)controller.getContentManager().extractContent(inform);
			if(ce instanceof PowerPlan){
				//process recieved result from the block
				PowerPlan nextDayPlan=(PowerPlan)ce;
				System.out.println(controller.getLocalName()+": power plan recieved");
				//printing th plan
				for(int i=0;i<nextDayPlan.getPlan().length;i++){
					System.out.printf("Hour %d planned value:%.2f\n",i,nextDayPlan.getPlan()[i]);
				}
				controller.setNextDayPlan(nextDayPlan);
				controller.setDayPlanRcvd(true);
				if(controller.isTodayPlanExecuted()){
					//set recieved plan as current day plan
					controller.setCurrDayPlan(nextDayPlan);
					controller.setDayPlanRcvd(false);
					controller.setTodayPlanExecuted(false);
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
