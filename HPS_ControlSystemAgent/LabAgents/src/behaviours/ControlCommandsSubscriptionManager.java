package behaviours;

import internalClasses.BlockAction;

import java.util.Vector;

import ontology.HPSOntology;
import ontology.HPSblockControlAction;
import ontology.HPSblockControlAction.ControlType;
import ontology.HPSblockDescriptor;
import ontology.HPSblockDescriptor.BlockState;
import agents.HPSblocksControlAgent;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;


public class ControlCommandsSubscriptionManager implements SubscriptionManager {
	private HPSblocksControlAgent controller;
	private final int NOTIFICATION_PERIOD=3000;
	private int currHour=0;
	
	public ControlCommandsSubscriptionManager(HPSblocksControlAgent controller) {
		super();
		this.controller = controller;
	}

	@Override
	public boolean register(Subscription s) throws RefuseException, NotUnderstoodException {
		boolean result=false;
		if(s.getMessage().getContent().equalsIgnoreCase("control-commands-subscription")){
			controller.addCcsubscription(s);
			System.out.println(controller.getLocalName()+": received subscription for "+s.getMessage().getContent()+" from "+s.getMessage().getSender().getLocalName());
			
			//add behavior for sending one hour notifications
			if (controller.getCcSubscription().size()==1) {
				controller.addBehaviour(new TickerBehaviour(controller,
						1000) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 3674681447400834233L;
					@Override
					protected void onTick() {
						if(!((HPSblocksControlAgent)myAgent).isTodayPlanExecuted()){
							//have job to do, call notify method
							((HPSblocksControlAgent) myAgent).getCcSubMngr().notify((HPSblocksControlAgent) myAgent);
							//setting tickert period 1 hour
							this.reset(NOTIFICATION_PERIOD);
						}
						else{
							//do not have plan, so do nothing and poll with small period 
							this.reset(1000);
						}						
					}
				});
			}
			//Subscribing for receiving production result each hour
			controller.addBehaviour(new OneHourResultSubscriptionBehaviour(controller,s.getMessage().getSender()));
			result=true;
		}
		return result;
	}

	@Override
	public boolean deregister(Subscription s) throws FailureException {
		System.out.println("Agent "+s.getMessage().getSender().getLocalName()+" canceled subscription.");
		return true;
	}
	/**
	 * Notify the subscriber
	 * @param a
	 */
	public void notify(HPSblocksControlAgent a){
		/**
		 * TODO update the notify method
		 */
		Vector<BlockAction> actions=distributePower(a.getBlocks(),a.getCurrDayPlan().getPlan()[this.currHour]);
		ACLMessage notification;
		for (Subscription s : a.getCcSubscription()) {
			notification = s.getMessage().createReply();
			notification.setPerformative(ACLMessage.INFORM);
			notification.setLanguage(new SLCodec().getName());
			notification.setOntology(HPSOntology.getInstance().getName());
			/**
			 * fill the fields of the message and send		
			 */
			try {
				Action act=new Action();
				HPSblockControlAction blockAction=this.setAction(s.getMessage().getSender(),actions);
				act.setActor(s.getMessage().getSender());
				act.setAction(blockAction);
				a.getContentManager().fillContent(notification, act);
				s.notify(notification);
				//update the state of the block
				BlockState newState;
				switch (blockAction.getActionType()){
				case Start:	
					newState=BlockState.Active;
					break;
				case Stop: 
					newState=BlockState.Passive;
					break;
				case SetPower: 
					newState=BlockState.Active;
					break;
				default:
					newState=a.getBlocks().get(a.getBlockIndex(s.getMessage().getSender())).getState();
					break;
				}
				a.getBlocks().get(a.getBlockIndex(s.getMessage().getSender())).setState(newState);
				
			} catch (CodecException e) {
				e.printStackTrace();
			} catch (OntologyException e) {
				e.printStackTrace();
			}
		}
		//incrementing curHour counter
		this.currHour++;
		if (this.currHour==24){
			this.currHour=0;
			if(a.isDayPlanRcvd()){
				a.setCurrDayPlan(a.getNextDayPlan());
				a.setDayPlanRcvd(false);
			}
			else a.setTodayPlanExecuted(true);
		}
	}
	private Vector<BlockAction> distributePower(Vector<HPSblockDescriptor> blocks, double power){
		/**
		 * Define new action for each block in blocks
		 */
		Vector<BlockAction> result=new Vector<BlockAction>();
		Vector<HPSblockDescriptor> activeBlocks=new Vector<HPSblockDescriptor>();
		//searching for damaged blocks
		for(HPSblockDescriptor b:blocks){
			if(!b.getState().equals(BlockState.Damage)){
				activeBlocks.add(b);
			}
			else{
				result.add(new BlockAction(b.getId(), new HPSblockControlAction(0, ControlType.NoAction)));
			}
		}
		double avgPower=power/activeBlocks.size();
		Vector<HPSblockDescriptor> tooLowPower=new Vector<HPSblockDescriptor>();
		//checking if the avg power lower than minP
		
		do {
			tooLowPower.removeAllElements();
			for (HPSblockDescriptor b : activeBlocks) {
				if (b.getMinP() > avgPower) {
					tooLowPower.add(b);
				}
			}
			if (tooLowPower.size() == 1) {				
				//stop this block and distribute the power for the rest
				HPSblockDescriptor stopBlock = tooLowPower.get(0);
				result.add(new BlockAction(stopBlock.getId(),
						new HPSblockControlAction(0, ControlType.Stop)));
				activeBlocks.remove(stopBlock);
				if(activeBlocks.size()>0) avgPower = power / activeBlocks.size();
				tooLowPower.remove(stopBlock);
			} else if (tooLowPower.size() > 1) {
				double largeMinP = -1;
				HPSblockDescriptor largeMinPBlock = new HPSblockDescriptor();
				//find the block with the largest minP and remove it
				for (HPSblockDescriptor b : activeBlocks) {
					if (b.getMinP() > largeMinP) {
						largeMinP = b.getMinP();
						largeMinPBlock = b;
					}
				}
				result.add(new BlockAction(largeMinPBlock.getId(),
						new HPSblockControlAction(0, ControlType.Stop)));
				activeBlocks.remove(largeMinPBlock);
				avgPower = power / activeBlocks.size();
			}
		} while (tooLowPower.size()!=0);
		//add actions for active blocks
		if(activeBlocks.size()>0){
			//distribute power between blocks evenly
			for(HPSblockDescriptor b:activeBlocks){
				//check the current state of the block
				if(b.getState().equals(BlockState.Passive)){
					//set control type to start
					result.add(new BlockAction(b.getId(), new HPSblockControlAction(avgPower, ControlType.Start)));
				}
				else{
					//set control type to setPower
					result.add(new BlockAction(b.getId(), new HPSblockControlAction(avgPower, ControlType.SetPower)));
				}
			}
		}
		return result;
	}
	private HPSblockControlAction setAction(AID blockId, Vector<BlockAction> actions)
	{
		/**
		 * select action from actions
		 */
		boolean actFound=false;
		HPSblockControlAction command = new HPSblockControlAction();
		for(BlockAction act:actions){
			if(act.blockId.equals(blockId)) {
				command=act.action;
				actFound=true;
			}
		}
		if(!actFound)System.out.println(controller.getLocalName()+": ERROR. Action for block "+blockId.getLocalName()+"wasn't found");
		return command;
	}
}
