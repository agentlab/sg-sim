package CsAgentForWB;

import java.util.Date;
import Ontologies.*;
import Behaviours.*;
import jade.core.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.content.onto.basic.Action;
import jade.domain.AMSService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;


public class PdAgent extends Agent {

	//Данный агент эмулирует работу агента-подстанции 
	
	private static final long serialVersionUID = 1L;
	private Codec codec = new SLCodec();
	private Ontology ontology = AgentForWbOntology.getInstance();

	public void setup() {    
		
		getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);

		System.out.println("PdAgent.setup()");
			
        ACLMessage aclmsg = new ACLMessage(ACLMessage.REQUEST);
		
		//int[] pwr = {-1, 2 , -3, -4, 3, 5};
        byte[] pwr = {-1, 2 , -3, -4};
        PlanMessage plan = new PlanMessage(); 
		plan.setPower(pwr);
		
		SendPlan sp = new SendPlan();
		sp.setMessage(plan);
		
		aclmsg.setLanguage(codec.getName());
		aclmsg.setOntology(ontology.getName());
		aclmsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		aclmsg.setReplyByDate(new Date(System.currentTimeMillis() + 90000));
		
		AMSAgentDescription[] agents = null; 
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1)); 
			
			agents = AMSService.search(this, new AMSAgentDescription(), c);
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}
				
		for (AMSAgentDescription agent : agents) {
			
			AID agentID = agent.getName();
			
			try {
				getContentManager().fillContent(aclmsg, new Action(agentID,sp));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(agentID.getLocalName().compareTo("ControlSystem") != 0) continue;
			
			aclmsg.clearAllReceiver();
			aclmsg.addReceiver(agentID);
			
			System.out.println("Sending to " + agentID);
			
			addBehaviour(new TestAgentInitiatorBehaviour(this, aclmsg)); 
		}
	    
	} 
}
