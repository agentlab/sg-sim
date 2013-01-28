package ParkBespAgents;

import java.util.Date;
import Ontologies.*;
import Behaviours.*;
import jade.core.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.Action;
import jade.domain.DFService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;


public class ParkBespilot extends Agent {

	//Данный агент эмулирует работу агента-подстанции 
	
	private static final long serialVersionUID = 1L;
	private Codec codec = new SLCodec();
	private Ontology ontology = PBOntology.getInstance();

	public void setup() {    
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		System.out.println("ParkBespilot.setup()");
			
        ACLMessage aclmsg = new ACLMessage(ACLMessage.REQUEST);
		
        byte[] pwr = {20 , 30 , 10, 50, 23,43,46,21,51,23,49,61,41,33,30 ,10, 50, 23,43,46,21,51,23,49};
        PlanMessage plan = new PlanMessage(); 
		plan.setPower(pwr);
		
		SendPlan sp = new SendPlan();
		sp.setMessage(plan);
		
		aclmsg.setLanguage(codec.getName());
		aclmsg.setOntology(ontology.getName());
		aclmsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		aclmsg.setReplyByDate(new Date(System.currentTimeMillis() + 9000));
		
		DFAgentDescription[] agents = null; 
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1)); 
			
			agents = DFService.search(this, new DFAgentDescription(), c);
		} catch (Exception e) {
			System.out.println("Problem searching DF: " + e);
			e.printStackTrace();
		}
				
		for (DFAgentDescription agent : agents) {
			
			AID agentID = agent.getName();
			
			try {
				getContentManager().fillContent(aclmsg, new Action(agentID,sp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(agentID.getLocalName().compareTo("ControlSystemParkBesp") != 0) continue;
			
			aclmsg.clearAllReceiver();
			aclmsg.addReceiver(agentID);
			
			System.out.println("Sending to " + agentID);
			
			addBehaviour(new TestAgentInitiatorBehaviour(this, aclmsg)); 
		}
	    
	} 
}