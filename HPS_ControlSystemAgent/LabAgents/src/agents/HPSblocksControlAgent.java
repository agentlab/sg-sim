package agents;


import ontology.*;
import behaviours.ControlCommandsSubscriptionManager;
import behaviours.OneDayResultSubscriptionManager;
import behaviours.PPRequestResponder;
import behaviours.SearchBlocksBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.AID;
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
	private HPSDescriptor myParams=new HPSDescriptor();									// Descriptor of HPS parameters
	private PowerPlan curDayResult=new PowerPlan();														// Current day generated power
	private PowerPlan nextDayPlan=new PowerPlan();														// Next day power plan
	private PowerPlan currDayPlan=new PowerPlan();
	private boolean dayPlanRcvd=false;
	private boolean todayPlanExecuted=true;
	public DFAgentDescription dfd=new DFAgentDescription();
	
	protected void setup(){
		/**
		 * initialize agent
		 */
				
		System.out.println("Hello, the HPSblocksControl agent "+getAID().getLocalName()+" is started");
		myParams.setHpsId(this.getAID());
		//register language and ontology
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(HPSOntology.getInstance());
		this.getContentManager().registerOntology(PowerProducerOntology.getInstance());
		
		//Registering services on yellow pages
		ServiceDescription sd= new ServiceDescription();
		sd.setType("power-generators-control");
		sd.setName("hpsController");
		this.dfd.setName(getAID());
		this.dfd.addServices(sd);
		try {
			DFService.register(this, this.dfd);
		} catch (FIPAException e) {
			e.printStackTrace();
		}
		
		//add serach blocks behaviour (oneShotBehaviour)
		this.addBehaviour(new SearchBlocksBehaviour());

		//adding the request responder for Power Producer request
		System.out.println(getLocalName()+": waiting for requests.");
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
	
	public OneDayResultSubscriptionManager getOdrSubMngr() {
		return odrSubMngr;
	}

	public void setOdrSubMngr(OneDayResultSubscriptionManager odrSubMngr) {
		this.odrSubMngr = odrSubMngr;
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

	public PowerPlan getNextDayPlan() {
		return nextDayPlan;
	}

	public void setNextDayPlan(PowerPlan nextDayPlan) {
		this.nextDayPlan = nextDayPlan;
	}	

	public boolean isDayPlanRcvd() {
		return dayPlanRcvd;
	}

	public void setDayPlanRcvd(boolean dayPlanRcvd) {
		this.dayPlanRcvd = dayPlanRcvd;
	}

	public PowerPlan getCurrDayPlan() {
		return currDayPlan;
	}

	public void setCurrDayPlan(PowerPlan currDayPlan) {
		this.currDayPlan = currDayPlan;
	}

	public boolean isTodayPlanExecuted() {
		return todayPlanExecuted;
	}

	public void setTodayPlanExecuted(boolean todayPlanExecuted) {
		this.todayPlanExecuted = todayPlanExecuted;
	}
	
	public int getBlockIndex(AID blockID){
		int result=-1;
		for(HPSblockDescriptor d:this.blocks){
			if(d.getId().equals(blockID)) result=this.blocks.indexOf(d);
		}
		return result;
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
