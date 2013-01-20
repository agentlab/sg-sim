package agents;

import behaviours.PowerPlansSubscriptionManager;
import behaviours.RequestTPPControllerParamsBehaviour;
import ontology.TPPDescriptor;
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
	private TPPDescriptor controllerDescriptor=new TPPDescriptor();
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
		
		//searching yellow pages for TPP controller agent
		ServiceDescription ssd=new ServiceDescription();
		ssd.setName("TPPController"); //MUST be set the same as for controller
		ssd.setType("TPP-generator-control"); //MUST be set the same as for controller
		DFAgentDescription template=new DFAgentDescription();
		template.addServices(ssd);
		try {
			DFAgentDescription[] searchResult;
			do searchResult=DFService.search(this, template);
			while (searchResult.length==0);
			
			if (searchResult.length>0){

				System.out.println(getLocalName()+": TPP controller found - "+searchResult[0].getName());
				this.controllerDescriptor.setTppId(searchResult[0].getName()); //using only first found controller
				this.addBehaviour(new RequestTPPControllerParamsBehaviour(this));
			}
			else{
				System.out.println("No TPP controllers agents were found.");
			}
		} catch (FIPAException e) {
				e.printStackTrace();
		}		
	}

	public TPPDescriptor getControllerDescriptor() {
		return controllerDescriptor;
	}

	public void setControllerDescriptor(TPPDescriptor controllerDescriptor) {
		this.controllerDescriptor = controllerDescriptor;
	}
	
	public Subscription getPpSubscription() {
		return ppSubscription;
	}

	public void setPpSubscription(Subscription ppSubscription) {
		this.ppSubscription = ppSubscription;
	}

	public PowerPlansSubscriptionManager getPpSubMngr() {
		return ppSubMngr;
	}

	public void setPpSubMngr(PowerPlansSubscriptionManager ppSubMngr) {
		this.ppSubMngr = ppSubMngr;
	}

	protected void takeDown(){
		//Printing goodbye message
		System.out.println("The PowerProducerAgent "+this.getAID().getLocalName()+" is terminated");
	}

}
