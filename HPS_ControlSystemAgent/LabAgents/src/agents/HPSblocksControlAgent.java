package agents;


import ontology.*;
import behaviours.ControlCommandsSubscriptionManager;
import behaviours.OneHourResultSubscriptionManager;
import behaviours.SearchBlocksBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
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
	
	private double currentPower; //power being generated
	private double maxPower; //maximum Power
	private Vector<HPSblockDescriptor> blocks = new Vector<HPSblockDescriptor>(); 		// Controlled blocks list
	private Vector<Subscription> ccSubscription= new Vector<Subscription>();
	private ControlCommandsSubscriptionManager ccSubMngr;
	
	protected void setup(){
		/**
		 * initialize agent
		 */
				
		System.out.println("Hello, the GPSBcontrol agent "+getAID().getLocalName()+" is started");
		
		//register language and ontology
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(HPSOntology.getInstance());
		
		//Registering service on yellow pages
		ServiceDescription sd= new ServiceDescription();
		sd.setType("power-generators-control");
		sd.setName("hpsController");
		DFAgentDescription dfd= new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//add serach blocks behaviour (oneShotBehaviour)
		this.addBehaviour(new SearchBlocksBehaviour());
		//adding the subscription responder
		this.ccSubMngr = new ControlCommandsSubscriptionManager(this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
		SubscriptionResponder ccSubResponder = new SubscriptionResponder(this, mt, this.ccSubMngr);
		this.addBehaviour(ccSubResponder);
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

	protected void takeDown(){
		/**
		 * terminating agent
		 */
		//Unregistering the service from yellow pages
		try {
			DFService.deregister(this);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Printing goodbye message
		System.out.println("The GPSBcontrol agent "+getAID().getLocalName()+" is terminated");
	}
	

}
