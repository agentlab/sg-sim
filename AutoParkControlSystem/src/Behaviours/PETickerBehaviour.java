package Behaviours;

import java.util.Date;

import Ontologies.ActionMessage;
import Ontologies.PEOntology;
import Ontologies.PlanMessage;
import Ontologies.SendAction;
import Ontologies.SendPlan;
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
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class PETickerBehaviour extends TickerBehaviour {

	private static final long serialVersionUID = 1L;
    private int step;
    private Agent myagent;
    private Codec codec = new SLCodec();
	private Ontology ontology = PEOntology.getInstance();
	private ACLMessage aclmsg;
	
	public PETickerBehaviour(Agent a, ACLMessage msg) {
		super(a, 3000);
		System.out.println("---------Initialize SendAction by Control System Agent---------");
		this.myagent=a;
		this.aclmsg = msg;
		this.step = 0;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onTick() {
		
		// TODO Auto-generated method stub
		ContentElement content;
		Concept concept = null;
		PlanMessage planM;
		
		try {
			content = myAgent.getContentManager().extractContent(aclmsg);
			concept = ((Action)content).getAction();
		} catch (CodecException | OntologyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SendPlan SP = (SendPlan) concept;
		planM = SP.getMessage();
				
        ACLMessage aclmsg1 = new ACLMessage(ACLMessage.REQUEST);
		
		aclmsg1.setLanguage(codec.getName());
		aclmsg1.setOntology(ontology.getName());
		aclmsg1.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
	    aclmsg1.setReplyByDate(new Date(System.currentTimeMillis() + 3000));
		  
	      ActionMessage am = new ActionMessage();
	    if (planM.getPower().length<(step+1)) this.stop();
	    else {
	      am.setAction(planM.getPower()[step]);
		  SendAction sa = new SendAction();
		  sa.setMessage(am);
		
		  DFAgentDescription[] agents = null; 
		  try {
		  	  SearchConstraints c = new SearchConstraints();
			  c.setMaxResults(new Long(-1)); 
			
			  agents = DFService.search(this.myAgent, new DFAgentDescription(), c);
		  } catch (Exception e) {
			  System.out.println("Problem searching DF: " + e);
	 		  e.printStackTrace();
	 	  }
				
		   for (DFAgentDescription agent : agents) {
			
			  AID agentID = agent.getName();
			
			
			  try {
				  myAgent.getContentManager().fillContent(aclmsg1, new Action(agentID, sa));
			  } catch (Exception e) {
				  e.printStackTrace();
			  }
			
			  if(agentID.getLocalName().compareTo("Park") != 0) continue;
			
			  aclmsg1.clearAllReceiver();
			  aclmsg1.addReceiver(agentID);
			  System.out.println(" ");
			  System.out.println("Current ActionMessage: " +planM.getPower()[step]+"  Hour: "+(step+1));
			  System.out.println("Sending "+step+" action to " + agentID);
			  step++;
			  myAgent.addBehaviour(new PERequestInitiatorBehaviour(myAgent, aclmsg1));
		   }
		   }
	      }
	}


