package agents;


import ontology.*;
import behaviours.ControlCommandsSubscriptionManager;
import behaviours.OneDayResultSubscriptionManager;
import behaviours.PPRequestResponder;
import behaviours.SearchBlocksBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;

import java.util.Vector;

public class HPSblocksControlAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9098546390730798576L;
	
	private Vector<HPSblockDescriptor> blocks = new Vector<HPSblockDescriptor>(); 		// Controlled HPS blocks parameters
	private Vector<Subscription> ccSubscription= new Vector<Subscription>();			// HPS blocks subscriptions for control commands
	private ControlCommandsSubscriptionManager ccSubMngr;								// SubMngr for control commands
	private OneDayResultSubscriptionManager odrSubMngr;									// SubMngr for one day results
	private Subscription odrSubscription;												// Power producer subscription for one day results
	private HPSDescriptor myParams;														// Descriptor of HPS parameters
	private PowerPlan curDayResult;														// Current day generated power
	private PowerPlan nextDayPlan;														// Next day power plan
	
	protected void setup(){
		/**
		 * initialize agent
		 */
				
		System.out.println("Hello, the HPSblocksControl agent "+getAID().getLocalName()+" is started");
		
		//register language and ontology
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(HPSOntology.getInstance());
		this.getContentManager().registerOntology(PowerProducerOntology.getInstance());
		
		//Registering services on yellow pages
		ServiceDescription sd= new ServiceDescription();
		sd.setType("power-generators-control");
		sd.setName("hpsController");
		DFAgentDescription dfd= new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		//add serach blocks behaviour (oneShotBehaviour)
		this.addBehaviour(new SearchBlocksBehaviour());

		//adding the control commands subscription responder
		this.ccSubMngr = new ControlCommandsSubscriptionManager(this);
		MessageTemplate ccMT = MessageTemplate.and(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),MessageTemplate.MatchOntology(HPSOntology.getInstance().getName()));
		SubscriptionResponder ccSubResponder = new SubscriptionResponder(this, ccMT, this.ccSubMngr);
		this.addBehaviour(ccSubResponder);
		
		//adding the request responder for Power Producer request
		System.out.println("Agent "+getLocalName()+" waiting for requests...");
	  	MessageTemplate template = MessageTemplate.and(
	  	MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
	  	MessageTemplate.MatchPerformative(ACLMessage.REQUEST));	
	  	this.addBehaviour(new PPRequestResponder(this, template));
	  	
	  	//adding one day result subscription responder
	  	this.odrSubMngr= new OneDayResultSubscriptionManager(this);
	  	MessageTemplate odrMT=MessageTemplate.and(SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE),MessageTemplate.MatchOntology(PowerProducerOntology.getInstance().getName()));
	  	SubscriptionResponder odrSubResponder = new SubscriptionResponder(this, odrMT, this.odrSubMngr);
	  	this.addBehaviour(odrSubResponder);
	}
	
	public void addBlock(HPSblockDescriptor hpsBlock){
		this.blocks.add(hpsBlock);
		return;
	}
	public void removeBlock(int n){
		this.blocks.remove(n);
		return;
	}
	public Vector<HPSblockDescriptor> getBlocks(){
		return this.blocks;
	}
	public void setBlocks(Vector<HPSblockDescriptor> list){
		this.blocks=list;
	}
	

	public ControlCommandsSubscriptionManager getCcSubMngr() {
		return ccSubMngr;
	}

	public void setCcSubMngr(ControlCommandsSubscriptionManager ccSubMngr) {
		this.ccSubMngr = ccSubMngr;
	}

	public Vector<Subscription> getCcSubscription() {
		return ccSubscription;
	}

	public void setCcSubscription(Vector<Subscription> ccSubscription) {
		this.ccSubscription = ccSubscription;
	}
	
	public void addCcsubscription(Subscription s){
		this.ccSubscription.add(s);
	}
	
	public HPSDescriptor getMyParams() {
		return myParams;
	}

	public void setMyParams(HPSDescriptor myParams) {
		this.myParams = myParams;
	}
	
	public Subscription getOdrSubscription() {
		return odrSubscription;
	}

	public void setOdrSubscription(Subscription odrSubscription) {
		this.odrSubscription = odrSubscription;
	}

	public PowerPlan getCurDayResult() {
		return curDayResult;
	}

	public void setCurDayResult(PowerPlan curDayResult) {
		this.curDayResult = curDayResult;
	}

	protected void takeDown(){
		/**
		 * terminating agent
		 */
		//Unregistering the service from yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		//Printing goodbye message
		System.out.println("The HPSblocsControl agent "+getAID().getLocalName()+" is terminated");
	}	
}
