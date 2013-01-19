package agents;

import behaviours.PowerPlansSubscriptionManager;
import behaviours.RequestAPSControllerParamsBehaviour;
import ontology.APSDescriptor;
import ontology.PowerProducerOntology;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
//import jade.lang.acl.ACLMessage;
//import jade.lang.acl.MessageTemplate;
//import jade.proto.SubscriptionResponder;
import jade.proto.SubscriptionResponder.Subscription;

public class PowerProducerAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8453859690369143213L;
	private APSDescriptor controllerDescriptor=new APSDescriptor();
	private PowerPlansSubscriptionManager ppSubMngr;
	private Subscription ppSubscription;
	
	protected void setup(){
		/**
		 * Initializing agent
		 */
		System.out.println("Hello, the PowerProducerAgent "+this.getAID().getLocalName()+" is started.");
		//registering language and ontology
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(PowerProducerOntology.getInstance());
		
		//searching yellow pages for APS controller agent
		ServiceDescription ssd=new ServiceDescription();
		ssd.setName("APSController"); //MUST be set the same as for controller
		ssd.setType("APS-generator-control"); //MUST be set the same as for controller
		DFAgentDescription template=new DFAgentDescription();
		template.addServices(ssd);
		try {
			DFAgentDescription[] searchResult;
			do searchResult=DFService.search(this, template);
			while (searchResult.length==0);
			
			if (searchResult.length>0){

				System.out.println(getLocalName()+": APS controller found - "+searchResult[0].getName());
				this.controllerDescriptor.setAPSId(searchResult[0].getName()); //using only first found controller
				this.addBehaviour(new RequestAPSControllerParamsBehaviour(this));
			}
			else{
				System.out.println("No APS controllers agents were found.");
			}
		} catch (FIPAException e) {
				e.printStackTrace();
		}		
	}

	public APSDescriptor getControllerDescriptor() {
		return controllerDescriptor;
	}

	public void setControllerDescriptor(APSDescriptor controllerDescriptor) {
		this.controllerDescriptor = controllerDescriptor;
	}
	
	public Subscription geAPSSubscription() {
		return ppSubscription;
	}

	public void seAPSSubscription(Subscription ppSubscription) {
		this.ppSubscription = ppSubscription;
	}

	public PowerPlansSubscriptionManager geAPSSubMngr() {
		return ppSubMngr;
	}

	public void seAPSSubMngr(PowerPlansSubscriptionManager ppSubMngr) {
		this.ppSubMngr = ppSubMngr;
	}

	protected void takeDown(){
		//Printing goodbye message
		System.out.println("The PowerProducerAgent "+this.getAID().getLocalName()+" is terminated");
	}

}
