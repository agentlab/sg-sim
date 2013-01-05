package behaviours;

import ontology.PowerProducerOntology;
import agents.HPSblocksControlAgent;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;

public class OneDayResultSubscriptionManager implements SubscriptionManager {

	private HPSblocksControlAgent controller;

	public OneDayResultSubscriptionManager(HPSblocksControlAgent controller) {
		super();
		this.controller = controller;
	}

	@Override
	public boolean register(Subscription s) throws RefuseException,
			NotUnderstoodException {
		boolean result=false;
		if(s.getMessage().getContent().equalsIgnoreCase("one-day-result-subscription")){
			controller.setOdrSubscription(s);
			System.out.println(controller.getLocalName()+": received subscription for "+s.getMessage().getContent()+" from "+s.getMessage().getSender().getLocalName());
			result=true;
		}
		return result;
	}
	
	@Override
	public boolean deregister(Subscription s) throws FailureException {
		System.out.println("Agent "+s.getMessage().getSender().getLocalName()+" canceled subscription.");
		return true;
	}
	public void notify(HPSblocksControlAgent a){
		ACLMessage notification = a.getOdrSubscription().getMessage().createReply();
		notification.setPerformative(ACLMessage.INFORM);
		notification.setLanguage(new SLCodec().getName()); 
		notification.setOntology(PowerProducerOntology.getInstance().getName());
		// Fill in the message content		
		try {
			a.getContentManager().fillContent(notification, a.getCurDayResult());
			a.getOdrSubscription().notify(notification); 
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}		
	}
}
