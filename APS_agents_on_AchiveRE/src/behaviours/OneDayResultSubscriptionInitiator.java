package behaviours;

import java.util.Vector;

import ontology.PowerPlan;
import jade.content.Predicate;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;

public class OneDayResultSubscriptionInitiator extends SubscriptionInitiator{

	private static final long serialVersionUID = -791397212235473619L;

	public OneDayResultSubscriptionInitiator(Agent a, ACLMessage msg) {
		super(a, msg);
		
	}
	
	@Override
	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("Warning. Agent "+refuse.getSender().getName()+" refused subscription.");
	}
	
	@Override
	protected void handleInform(ACLMessage inform) {
		/**
		 * handle recieved notification message
		 */
		try {
			Predicate ce=(Predicate)myAgent.getContentManager().extractContent(inform);
			if(ce instanceof PowerPlan){
				//process recieved result from the controller
				PowerPlan plan=(PowerPlan)ce;
				System.out.println(myAgent.getLocalName()+": recieved one day result from "+inform.getSender().getName());
				//printing plan
				for(int i=0;i<plan.getPlan().length;i++){
					System.out.printf("Hour %d produced value:%.2f\n",i,plan.getPlan()[i]);
				}
				double sum=0;
				for(int i=0;i<plan.getPlan().length;i++){
					sum+=plan.getPlan()[i];
				}
				System.out.printf("Total generated power on %tF is %.2f",plan.getDate(),sum);
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
