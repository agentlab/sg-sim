package Behaviours;

import java.util.Date;
import Ontologies.*;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class PBRequestResponderBehaviour extends AchieveREResponder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	public PBRequestResponderBehaviour(Agent a, MessageTemplate mt) {
		super(a, mt);
		// TODO Auto-generated constructor stub
	}
	
	protected ACLMessage prepareResponse(ACLMessage msg) throws NotUnderstoodException, RefuseException {
		boolean agreed = false;
		ContentElement content;
		Concept action = null;
		try {
			content = myAgent.getContentManager().extractContent(msg);
			action = ((Action)content).getAction();
			
			if(action instanceof SendPlan) {
				System.out.println("CSParkBespAgent Received SendPlan! All right!");
				agreed = true;
			} else if (action instanceof SendAction) {
				System.out.println("CSParkBespAgent Received SendAction! What are you doing? >(");
				agreed = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(agreed) {
			ACLMessage agree = msg.createReply();
			agree.setPerformative(ACLMessage.AGREE);
			return agree;
		} else {
			throw new RefuseException("unknown operation");
		}
	}
	
	protected ACLMessage prepareResultNotification(ACLMessage msg, ACLMessage response) throws FailureException {
		boolean performed = false;
		ContentElement content;
		Concept action = null;
		try {
			content = myAgent.getContentManager().extractContent(msg);
			action = ((Action)content).getAction();
			
			if(action instanceof SendPlan) {
				System.out.println("Performing SendPlan");
				this.performSendPlan(msg);
				performed = true;
				//
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		if(performed) {
			System.out.println("Agent "+myAgent.getLocalName()+": Action successfully performed");
			ACLMessage inform = msg.createReply();
			inform.setPerformative(ACLMessage.INFORM);
			return inform;
		} else {
			throw new FailureException("error");
		}
	}
	
	protected void performSendPlan(ACLMessage msg){
		
		myAgent.addBehaviour(new PBTickerBehaviour(myAgent, msg));
	}

}
