package behaviours;

import internalClasses.BlockPowerPlan;

import java.util.Date;
import java.util.Vector;

import agents.APSblocksControlAgent;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = -8339846306126568214L;
	private static final Vector<BlockPowerPlan> blockPlans=new Vector<BlockPowerPlan>();
	
	public OneHourResultSubscriptionInitiator(Agent a, ACLMessage msg) {
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
			if(ce instanceof OneHourResult){
				//process recieved result from the block
				OneHourResult res=(OneHourResult)ce;
				System.out.println(myAgent.getLocalName()+": result recieved from "+inform.getSender().getLocalName()+" at "+((OneHourResult)ce).getDate());
				System.out.println(myAgent.getLocalName()+": generated power by agent "+inform.getSender().getLocalName()+" : "+res.getPower());
				if(!checkId(inform.getSender())){
					//add sender to the blockPlans
					blockPlans.add(new BlockPowerPlan(inform.getSender()));
				}
				addResult2Plan(inform.getSender(),res.getPower());
				if(checkPlanLength()){
					for(BlockPowerPlan bp:blockPlans)bp.i=0;
					//get the resulting plan and call notify for PowerProducer
					double sum;
					for(int k=0;k<24;k++){
						sum=0;
						for(BlockPowerPlan bp:blockPlans){
							sum+=bp.plan[k];
						}
						((APSblocksControlAgent)myAgent).getCurDayResult().setPower(sum, k);
					}
					((APSblocksControlAgent)myAgent).getCurDayResult().setDate(new Date());
					//calling notify method
					((APSblocksControlAgent)myAgent).getOdrSubMngr().notify((APSblocksControlAgent) myAgent);
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
	private boolean checkId(AID id){
		boolean result=false;
		for(BlockPowerPlan bp:blockPlans){
			if(bp.blockId.equals(id)) result=true;
		}
		return result;
	}
	
	private void addResult2Plan(AID sender, double power) {
		for(BlockPowerPlan bp:blockPlans){
			if(bp.blockId.equals(sender)){
				bp.plan[bp.i]=power;
				bp.i++;
				return;
			}
		}
	}
	private boolean checkPlanLength() {
		for(BlockPowerPlan bp:blockPlans){
			if(bp.i!=24){
				return false;
			}
		}
		return true;
	}
}
