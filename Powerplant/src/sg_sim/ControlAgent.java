package sg_sim;

import java.util.Date;
import behaviours.*;

import ontologies.*;
import jade.content.lang.sl.*;
import jade.content.lang.*;
import jade.content.onto.Ontology;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.AMSService;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.*;
import jade.domain.DFService;

import jade.lang.acl.ACLMessage;


public class ControlAgent extends Agent {
	/**
	 * 
	 */
	private Codec codec = new SLCodec(); 
	private Ontology ontology = PowerplantOntology.getInstance(); //�������� ����������� ������� ��������� � �����
	
	private static final long serialVersionUID = 6615714058341878330L; 
	public void setup() { 
		System.out.println("ControlAgent.setup()");
		
		getContentManager().registerLanguage(codec); //����������� ����� � ���������
		getContentManager().registerOntology(ontology);
		
		//����������� ���������
		ACLMessage aclmsg = new ACLMessage(ACLMessage.REQUEST);		
		AssignPowerRequest apr = new AssignPowerRequest();
		apr.setAssignedPower(10880.58);
		
		aclmsg.setLanguage(codec.getName());
		aclmsg.setOntology(ontology.getName());
		aclmsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		aclmsg.setReplyByDate(new Date(System.currentTimeMillis() + 30000));
		
		DFAgentDescription[] agents = null; 
		
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1)); 
			
			agents = DFService.search(this, new DFAgentDescription(), c);
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}
		
		for (DFAgentDescription agent : agents) {
			
			AID agentID = agent.getName();
			
			try {
				getContentManager().fillContent(aclmsg, new Action(agentID, apr));
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(agentID.getLocalName());
			
			if(agentID.getLocalName().compareTo("Powerplant") != 0) continue;
			
			aclmsg.clearAllReceiver();
			aclmsg.addReceiver(agentID);
			
			System.out.println("Sending to " + agentID);
			
			addBehaviour(new ControlAgentRequestInitiatorBehaviour(this, aclmsg)); 
			
			try {
				Thread.yield();
				Thread.sleep(5000);
			} catch (Exception e) { System.out.println("INTERRUPT"); } 
			
			ACLMessage aclmsg2 = new ACLMessage(ACLMessage.REQUEST);		
			BeginPowerTransitionRequest bptr = new BeginPowerTransitionRequest();
			bptr.setAssignedPowerValid(false);
			
			aclmsg2.setLanguage(codec.getName());
			aclmsg2.setOntology(ontology.getName());
			aclmsg2.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			aclmsg2.setReplyByDate(new Date(System.currentTimeMillis() + 30000));
			
			try {
				getContentManager().fillContent(aclmsg2, new Action(agentID, bptr));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			aclmsg2.clearAllReceiver();
			aclmsg2.addReceiver(agentID);
			
			System.out.println("Sending to " + agentID);
			
			addBehaviour(new ControlAgentRequestInitiatorBehaviour(this, aclmsg2)); 
		}
	}

}