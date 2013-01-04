package agents;

import behaviours.ControlCommandsSubscriptionBehaviour;
import behaviours.OneHourResultSubscriptionManager;
import ontology.*;
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

public class HPSblockAgent extends Agent {

	/**
	 * 
	 */
	private HPSblockDescriptor params=new HPSblockDescriptor(null, 100*Math.random(), 1000*Math.random(),  "passive", 0);;
	private static final long serialVersionUID = 3716704426673119046L;
	private Subscription resultSubscription;
	private OneHourResultSubscriptionManager subMngr;
	
	protected void setup(){
		/**
		 * initialize agent
		 */
		
		System.out.println("Hello, the HPSblock agent "+getAID().getLocalName()+" is started");
		params.setId(getAID());
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(HPSOntology.getInstance());
				
		//Registering service on yellow pages
		ServiceDescription sd= new ServiceDescription();
		sd.setType("power-generator");
		sd.setName("hpsBlock");
		DFAgentDescription dfd= new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		//adding the request responder behavior
		System.out.println("Agent "+getLocalName()+" waiting for requests...");
	  	MessageTemplate template = MessageTemplate.and(
	  	MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
	  	MessageTemplate.MatchPerformative(ACLMessage.REQUEST));	  	
	  		
		this.addBehaviour(new AchieveREResponder(this, template) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 3224171302554807976L;

			protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
				System.out.println("Agent "+getLocalName()+": REQUEST received from "+request.getSender().getName()+". Action is "+request.getContent());
				if (request.getContent().equalsIgnoreCase("params-request")) {
					//do not send agree message, reply with inform	
					return null;
				}
				else {
					// We refuse to perform the action
					System.out.println("Agent "+getLocalName()+": Refuse");
					throw new RefuseException("check-failed");
				}
			}
				
			
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
					System.out.println("Agent "+getLocalName()+": Replying with parameter");
					ACLMessage inform = request.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					//send block parameters
					try {
						myAgent.getContentManager().fillContent(inform, params);
						//subscribing to the controller for commands
						myAgent.addBehaviour(new ControlCommandsSubscriptionBehaviour((HPSblockAgent)myAgent,request.getSender()));
					} catch (CodecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return inform;	
			}
			} );
		//adding the subscription responder
		this.subMngr = new OneHourResultSubscriptionManager(this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
		SubscriptionResponder subResponder = new SubscriptionResponder(this, mt, this.subMngr);
		this.addBehaviour(subResponder);
	}
	public HPSblockDescriptor getParams(){
		return this.params;
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
		System.out.println("The HPSblock agent "+getAID().getLocalName()+" is terminated");
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
}
