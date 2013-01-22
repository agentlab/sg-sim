package agents;

import behaviours.ControlCommandsSubscriptionBehaviour;
import behaviours.OneHourResultSubscriptionManager;
import ontology.*;
import ontology.APSblockDescriptor.BlockState;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;

public class APSblockAgent extends Agent {
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = -7838901911691186799L;
	private APSblockDescriptor params=new APSblockDescriptor(null, 100+10*(Math.random()-0.1), 1000+100*(Math.random()-0.1),  BlockState.Passive);
	private Subscription resultSubscription;
	private OneHourResultSubscriptionManager subMngr;
	private APSblockControlAction currentAction=new APSblockControlAction();
	private APSblockControlAction nextHourAction=new APSblockControlAction();
	private boolean firstCommand=true;
	private boolean startResultNotification=false;
	
	protected void setup(){
		/**
		 * initialize agent
		 */
		
		System.out.println(String.format("Hello, APS control agent %s activated", getAID().getLocalName()));
		params.setId(getAID());
		System.out.printf("%s: minPower: %.2f; maxPower: %.2f\n",getLocalName(),params.getMinP(),params.getMaxP());
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(APSOntology.getInstance());
				
		//Registering service
		ServiceDescription sd= new ServiceDescription();
		sd.setType("thermal-power-generator");
		sd.setName("APSBlock");
		DFAgentDescription dfd= new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
				
		//adding the request responder behavior
		System.out.println(getLocalName()+": is waiting for requests.");
	  	MessageTemplate template = MessageTemplate.and(
	  	MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
	  	MessageTemplate.MatchPerformative(ACLMessage.REQUEST));	  	
	  		
		this.addBehaviour(new AchieveREResponder(this, template) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 7191864547849030999L;
			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
				System.out.println(getLocalName()+": request received from "+request.getSender().getLocalName()+". Action is "+request.getContent());
				if (request.getContent().equalsIgnoreCase("params-request")) {
					//do not send agree message, reply with inform	
					//adding the subscription responder
					((APSblockAgent)myAgent).subMngr = new OneHourResultSubscriptionManager((APSblockAgent)myAgent);
					MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
					SubscriptionResponder subResponder = new SubscriptionResponder(myAgent, mt, ((APSblockAgent)myAgent).subMngr);
					myAgent.addBehaviour(subResponder);
					return null;
				}
				else {
					// We refuse to perform the action
					System.out.println("Agent "+getLocalName()+": Refuse");
					throw new RefuseException("check-failed");
				}
			}
				
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
					System.out.println(getLocalName()+": replying with parameters");
					ACLMessage inform = request.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					//send block parameters
					try {
						myAgent.getContentManager().fillContent(inform, params);
						//subscribing to the controller for commands
						myAgent.addBehaviour(new ControlCommandsSubscriptionBehaviour((APSblockAgent)myAgent,request.getSender()));
					} catch (CodecException e) {
						e.printStackTrace();
					} catch (OntologyException e) {
						e.printStackTrace();
					}
					
					return inform;	
			}
			} );
		
	}
	public APSblockDescriptor getParams(){
		return this.params;
	}
	protected void takeDown(){
		/**
		 * terminating agent
		 */
		//Unregistering the service
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Printing goodbye message
		System.out.println("APS control agent "+getAID().getLocalName()+" is terminated");
	}
	public Subscription getResultSubscription() {
		return resultSubscription;
	}
	public void setResultSubscription(Subscription resultSubscription) {
		this.resultSubscription = resultSubscription;
	}
	public OneHourResultSubscriptionManager getSubMngr() {
		return subMngr;
	}
	public void setSubMngr(OneHourResultSubscriptionManager subMngr) {
		this.subMngr = subMngr;
	}
	public APSblockControlAction getCurrentAction() {
		return currentAction;
	}
	public void setCurrentAction(APSblockControlAction currentAction) {
		this.currentAction = currentAction;
	}
	public APSblockControlAction getNextHourAction() {
		return nextHourAction;
	}
	public void setNextHourAction(APSblockControlAction nextHourAction) {
		this.nextHourAction = nextHourAction;
	}
	public boolean isFirstCommand() {
		return firstCommand;
	}
	public void setFirstCommand(boolean firstCommand) {
		this.firstCommand = firstCommand;
	}
	public boolean isStartResultNotification() {
		return startResultNotification;
	}
	public void setStartResultNotification(boolean startResultNotification) {
		this.startResultNotification = startResultNotification;
	}
}
