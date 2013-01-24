package ru.bmstu.agents;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.acl.LastOwnerException;
import java.util.Random;

import org.apache.log4j.Logger;

import ru.bmstu.objectToSell.Electricity;
import ru.bmstu.ontology.Energy;
import ru.bmstu.ontology.EnergyOntology;

import jade.content.AgentAction;
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
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
public class BabushkaAgent extends Agent {
private static final Logger logger = Logger.getLogger(BabushkaAgent.class);
private static final long serialVersionUID = 3663966406239393054L;
private Codec codec = new SLCodec();
private Ontology ontology = EnergyOntology.getInstance();


void sendMessage(Agent myAgent,int performative, AgentAction action, AID server,ACLMessage _msg) 
{
	
	AMSAgentDescription[] agents = null;
	AID agentID=null;
	try {
		SearchConstraints c = new SearchConstraints();
		c.setMaxResults(new Long(-1));
		agents = AMSService.search(myAgent, new AMSAgentDescription(), c);
	} catch (Exception e) {
		System.out.println("Problem searching AMS: " + e);
		e.printStackTrace();
	}
	for (AMSAgentDescription agent : agents) {
		if(agent.getName().getLocalName().equals("babushka"))
		{
			agentID = agent.getName();
		}
	}
	
	
	ACLMessage msg = new ACLMessage(performative);
	if(_msg!=null)
		msg=_msg;
	msg.setPerformative(performative);
	msg.setLanguage(codec.getName());
	msg.setOntology(ontology.getName());
	msg.setConversationId("Babushka");
	 try {
		 if(server!=null)
		 {	
			 System.out.println("est server");
			 msg.addReceiver(server);
			 getContentManager().fillContent(msg, new Action(server, action));
		 }else
		 {
			 System.out.println("net server");
			 getContentManager().fillContent(msg, new Action(agentID, action));
		 }
	 		
	 	send(msg);
	 }catch (Exception e){
		 e.printStackTrace();
	 }
}

protected void setup()  {
	
    // Register language and ontology
    getContentManager().registerLanguage(codec);
    getContentManager().registerOntology(ontology);

	
addBehaviour(new CyclicBehaviour(this) {
boolean canSetNewTime=true;
long lastTime=0;
private static final long serialVersionUID = -1912882200351395625L;
Electricity energy=new Electricity(0, 0);
long money=0;
boolean messageForBuyIsSend=false;

public void action()  {
	
	
	if(canSetNewTime)
	{
		lastTime=System.currentTimeMillis()+test.BabushkaBuyElectricity;
		canSetNewTime=false;
	}

	if(System.currentTimeMillis()>=lastTime)
	{
		energy= new Electricity(0, 0);
		money=money+800;//зарплата бабушки
		
		canSetNewTime=true;
		messageForBuyIsSend=false;
	}
	
	
	if(energy.getQuantity()==0&&!messageForBuyIsSend)
	{
		AMSAgentDescription[] agents = null;
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = AMSService.search(this.myAgent, new AMSAgentDescription(), c);
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}
		for (AMSAgentDescription agent : agents) {
			if(agent.getName().getLocalName().equals("broker"))
			{
				AID agentID = agent.getName();
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setConversationId("Babushka");
				msg.addReceiver(agentID);// id агента которому отправл€ем сообщение
				
				//новое
				
				Energy mo = new Energy();
				Random r = new Random();
				int needToBuy =50+ r.nextInt(50);
				mo.setNeedToBuy(String.valueOf(needToBuy));
				 
				 sendMessage(myAgent,ACLMessage.REQUEST, mo,agentID,null);
				 messageForBuyIsSend=true;
				//новое
				
				/** старое без онтологий
				msg.setLanguage("English");// язык сообщени€
				Random r = new Random();
				int needToBuy =50+ r.nextInt(50);
				msg.setContent(String.valueOf(needToBuy));
				send(msg); // отправл€ем сообщение
				messageForBuyIsSend=true;
				**/

			}
		}
	}
	
	MessageTemplate mt =MessageTemplate.MatchConversationId("Babushka");
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) {
			System.out.println("prishlo");
					if(msg.getPerformative()==ACLMessage.CONFIRM)
					{
						int price=0;
						int quantity=0;
						
						ContentElement content=null;
						try {
							content = getContentManager().extractContent(msg);
							Energy mo = (Energy)((Action)content).getAction();
							price=mo.getPrice();
							quantity=mo.getQuantity();
							money=money-mo.getPrice();
							energy.setQuantity(mo.getQuantity());
							System.out.println("energy of Babushka= "+energy.getQuantity());
							System.out.println("money of babushka = "+money);
						} catch (UngroundedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						 ACLMessage reply = msg.createReply();
						 Energy mo = new Energy();
						 mo.setPrice(price);
						 mo.setQuantity(quantity); 
						 sendMessage(myAgent,ACLMessage.AGREE, mo,null,reply);
						//reply.setContentObject(new Electricity(((Electricity)msg.getContentObject()).getPrice(), (((Electricity)msg.getContentObject()).getQuantity())));
					}

                }
/*
				else{
                		block();

                	}
*/
}});
}
}
		

