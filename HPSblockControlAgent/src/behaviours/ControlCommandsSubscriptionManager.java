package behaviours;

import ontology.HPSOntology;
import ontology.HPSblockControlAction;
import ontology.HPSblockControlAction.ControlType;
import agents.HPSblocksControlAgent;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;


public class ControlCommandsSubscriptionManager implements SubscriptionManager {
	private HPSblocksControlAgent controller;
	private final int NOTIFICATION_PERIOD=5000;
	
	public ControlCommandsSubscriptionManager(HPSblocksControlAgent controller) {
		super();
		this.controller = controller;
	}

	@Override
	public boolean register(Subscription s) throws RefuseException, NotUnderstoodException {
		boolean result=false;
		if(s.getMessage().getContent().equalsIgnoreCase("control-commands-subscription")){
			controller.addCcsubscription(s);
			System.out.println("Agent "+controller.getLocalName()+" has received subscription for "+s.getMessage().getContent()+" from "+s.getMessage().getSender().getLocalName());
			
			//add behavior for sending one hour notifications
			if (controller.getCcSubscription().size()==1) {
				controller.addBehaviour(new TickerBehaviour(controller,
						NOTIFICATION_PERIOD) {
					/**
					 * 
					 */
					private static final long serialVersionUID = 3674681447400834233L;
					@Override
					protected void onTick() {
						// TODO Auto-generated method stub					
						((HPSblocksControlAgent) myAgent).getCcSubMngr().notify((HPSblocksControlAgent) myAgent);
					}
				});
			}
			//Subscribing for receiving production result each hour
			controller.addBehaviour(new OneHourResultSubscriptionBehaviour(controller,s.getMessage().getSender()));
			result=true;
		}
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public boolean deregister(Subscription s) throws FailureException {
		// TODO Auto-generated method stub
		System.out.println("Agent "+s.getMessage().getSender().getLocalName()+" canceled subscription.");
		return true;
	}
	/**
	 * Notify the subscriber
	 * @param a
	 */
	public void notify(HPSblocksControlAgent a){
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
				HPSblockControlAction result = new HPSblockControlAction();
				result.setPower(500*Math.random());
				result.setcType(ControlType.Action2);
				result.setNum((int)Math.round(100*Math.random()));
				Action act=new Action();
				act.setActor(s.getMessage().getSender());
				act.setAction(result);
				a.getContentManager().fillContent(notification, act);
				s.notify(notification);
			} catch (CodecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OntologyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}

}
