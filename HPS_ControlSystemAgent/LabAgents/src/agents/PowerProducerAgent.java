package agents;

import behaviours.PowerPlansSubscriptionManager;
import behaviours.RequestHPSControllerParamsBehaviour;
import ontology.HPSDescriptor;
import ontology.PowerProducerOntology;
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

public class PowerProducerAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8242155058711516669L;
	private HPSDescriptor controllerDescriptor=new HPSDescriptor();
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
		
		//searching yellow pages for HPS controller agent
		ServiceDescription ssd=new ServiceDescription();
		ssd.setName("hpsController"); //MUST be set the same as for controller
		ssd.setType("HPS-Control"); //MUST be set the same as for controller
		DFAgentDescription template=new DFAgentDescription();
		template.addServices(ssd);
		try {
			DFAgentDescription[] searchResult;
			do searchResult=DFService.search(this, template);
			while (searchResult.length==0);
			
			if (searchResult.length>0){
				/**
				 * HPS controller found
				 * request parameters
				 */
				System.out.println(getLocalName()+": HPS controller found - "+searchResult[0].getName());
				this.controllerDescriptor.setHpsId(searchResult[0].getName()); //using only first found controller
				this.addBehaviour(new RequestHPSControllerParamsBehaviour(this));
			}
			else{
				System.out.println("No HPS controllers agents were found.");
			}
		} catch (FIPAException e) {
				e.printStackTrace();
		}		
	}

	public HPSDescriptor getControllerDescriptor() {
		return controllerDescriptor;
	}

	public void setControllerDescriptor(HPSDescriptor controllerDescriptor) {
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
