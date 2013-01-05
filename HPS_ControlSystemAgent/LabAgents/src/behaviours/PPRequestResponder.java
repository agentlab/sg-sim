package behaviours;

import agents.HPSblocksControlAgent;
import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class PPRequestResponder extends AchieveREResponder {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6974064784001782028L;

	private HPSblocksControlAgent controller;
	public PPRequestResponder(Agent a, MessageTemplate mt) {
		super(a, mt);
		this.controller=(HPSblocksControlAgent)a;
	}
	protected ACLMessage prepareResponse(ACLMessage request) throws NotUnderstoodException, RefuseException {
		System.out.println(controller.getLocalName()+": REQUEST received from "+request.getSender().getName()+". Action is "+request.getContent());
		if (request.getContent().equalsIgnoreCase("HPScontroller-params-request")) {
			//do not send agree message, reply with inform	
			return null;
		}
		else {
			// We refuse to perform the action
			System.out.println("Agent "+controller.getLocalName()+": Refuse");
			throw new RefuseException("check-failed");
		}
	}
		
	
	protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response){
			System.out.println(controller.getLocalName()+": Replying with parameters");
			ACLMessage inform = request.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			//send HPS parameters
			try {
					//TODO Check if myParams has been filled
					myAgent.getContentManager().fillContent(inform,
							controller.getMyParams());
					//subscribing to the Power Producer for daily plans commands
					myAgent.addBehaviour(new PowerPlansSubscriptionBehaviour(
							controller, request.getSender()));
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			return inform;	
	}

}
