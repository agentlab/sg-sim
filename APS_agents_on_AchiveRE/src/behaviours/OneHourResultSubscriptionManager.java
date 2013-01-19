package behaviours;

import java.util.Date;

import ontology.APSOntology;
import ontology.OneHourResult;
import agents.APSblockAgent;
//import agents.APSblocksControlAgent;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;


public class OneHourResultSubscriptionManager implements SubscriptionManager {
	private APSblockAgent block;
	private final int NOTIFICATION_PERIOD=3000;
	
	public OneHourResultSubscriptionManager(APSblockAgent block) {
		super();
		this.block = block;
	}

	@Override
	public boolean register(Subscription subscr) throws RefuseException,
			NotUnderstoodException {
		boolean result=false;
		if(subscr.getMessage().getContent().equalsIgnoreCase("one-hour-result-subscription")){
			block.setResultSubscription(subscr);
			System.out.println(block.getLocalName()+": received subscription for "+subscr.getMessage().getContent()+" from "+subscr.getMessage().getSender().getLocalName());
			//уведомления каждый period
			long period = 1000;
			block.addBehaviour(new TickerBehaviour(block,period) {
				/**
				 * 
				 */
				private static final long serialVersionUID = 3674681447400834233L;
				@Override
				protected void onTick() {
					//do nothing until first command received
					if(!((APSblockAgent)myAgent).isFirstCommand()){
						//set the beginning result notification flag and set new period
						if(!((APSblockAgent)myAgent).isStartResultNotification()){
							((APSblockAgent)myAgent).setStartResultNotification(true);
							((APSblockAgent)myAgent).setCurrentAction(((APSblockAgent)myAgent).getNextHourAction());
							this.reset(NOTIFICATION_PERIOD);
						}
						else ((APSblockAgent)myAgent).getSubMngr().notify((APSblockAgent)myAgent);
					}
					
				}
			});
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
	 */
	public void notify(APSblockAgent agnt){
			
		ACLMessage notification = agnt.getResultSubscription().getMessage().createReply();
		notification.setPerformative(ACLMessage.INFORM);
		notification.setLanguage(new SLCodec().getName()); 
		notification.setOntology(APSOntology.getInstance().getName());
		// Create context		
		OneHourResult result = new OneHourResult(block.getCurrentAction().getPower()*(1-0.02*(Math.random()-0.8)), new Date());
		try {
			agnt.getContentManager().fillContent(notification, result);
			agnt.getResultSubscription().notify(notification); 
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		block.setCurrentAction(block.getNextHourAction());	
	}

}
