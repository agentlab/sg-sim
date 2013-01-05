package behaviours;

import java.util.Date;

import ontology.HPSOntology;
import ontology.OneHourResult;
import agents.HPSblockAgent;
import agents.HPSblocksControlAgent;
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
	private HPSblockAgent block;
	private final int NOTIFICATION_PERIOD=3000;
	
	public OneHourResultSubscriptionManager(HPSblockAgent block) {
		super();
		this.block = block;
	}

	@Override
	public boolean register(Subscription s) throws RefuseException,
			NotUnderstoodException {
		boolean result=false;
		if(s.getMessage().getContent().equalsIgnoreCase("one-hour-result-subscription")){
			block.setResultSubscription(s);
			System.out.println(block.getLocalName()+": received subscription for "+s.getMessage().getContent()+" from "+s.getMessage().getSender().getLocalName());
			//add behavior for sending one hour notifications
			block.addBehaviour(new TickerBehaviour(block,1000) {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 3674681447400834233L;

				@Override
				protected void onTick() {
					//do nothing until first command received
					if(!((HPSblockAgent)myAgent).isFirstCommand()){
						//set the beginning result notification flag and set new period
						if(!((HPSblockAgent)myAgent).isStartResultNotification()){
							((HPSblockAgent)myAgent).setStartResultNotification(true);
							((HPSblockAgent)myAgent).setCurrentAction(((HPSblockAgent)myAgent).getNextHourAction());
							this.reset(NOTIFICATION_PERIOD);
						}
						else ((HPSblockAgent)myAgent).getSubMngr().notify((HPSblockAgent)myAgent);
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
	 * @param a
	 */
	public void notify(HPSblockAgent a){
			
		ACLMessage notification = a.getResultSubscription().getMessage().createReply();
		notification.setPerformative(ACLMessage.INFORM);
		notification.setLanguage(new SLCodec().getName()); 
		notification.setOntology(HPSOntology.getInstance().getName());
		// Create context		
		OneHourResult result = new OneHourResult(block.getCurrentAction().getPower()*(1+0.02*(Math.random()-0.5)), new Date());
		try {
			a.getContentManager().fillContent(notification, result);
			a.getResultSubscription().notify(notification); 
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}
		block.setCurrentAction(block.getNextHourAction());	
	}

}
