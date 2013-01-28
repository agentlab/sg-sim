package ru.bmstu.agents;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import java.util.Random;

import org.apache.log4j.Logger;

import ru.bmstu.objectToSell.Electricity;

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


import java.io.IOException;

import ru.bmstu.objectToSell.Electricity;
import ru.bmstu.ontology.Energy;
import ru.bmstu.ontology.EnergyOntology;
import ru.bmstu.simpleObj.ToSendObject;

public class BrokerReplyer extends Agent {
	private static final Logger logger = Logger.getLogger(BrokerReplyer.class);
	private Codec codec = new SLCodec();
	private Ontology ontology = EnergyOntology.getInstance();

	
	void sendMessage(int performative, AgentAction action,String conversId,ACLMessage _msg) 
	{	
		getContentManager().registerLanguage(codec);
	    getContentManager().registerOntology(ontology);
		ACLMessage msg = new ACLMessage(performative);
		if(_msg!=null)
		{
			msg=_msg;
		}
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		msg.setConversationId(conversId);
		 try {
			 
			 getContentManager().fillContent(msg, action);
		 	send(msg);
		 }catch (Exception e){
			 e.printStackTrace();
		 }
	}
	
	public ToSendObject messageListener(Agent myAgent, Electricity energy,int money, String conversationID)
	{	
		getContentManager().registerLanguage(codec);
	    getContentManager().registerOntology(ontology);

		MessageTemplate mt =MessageTemplate.MatchConversationId(conversationID);
		ACLMessage msg = myAgent.receive(mt);
		if (msg != null) 
		{ 
			if(msg.getPerformative()==ACLMessage.REQUEST){
				
				ContentElement content=null;
				try {
					content = getContentManager().extractContent(msg);
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
		    Energy mo = (Energy)((Action)content).getAction();
		    
			String quantity_to_sail= mo.getNeedToBuy();
			System.out.println(conversationID+" hochet kupit "+quantity_to_sail);
			System.out.println("tekushaya cena"+ conversationID +" "+energy.getPrice());
			int priceForNeedQuantity=Integer.parseInt(quantity_to_sail)*(energy.getPrice()+1);
			System.out.println("cena dlya "+conversationID+" "+(energy.getPrice()+1));
			
			ACLMessage reply = msg.createReply();
			
			Energy answer= new Energy();
			//answer.setNeedToBuy("ddsd");
			answer.setPrice(priceForNeedQuantity);
			answer.setQuantity(Integer.parseInt(quantity_to_sail));

			//sendMessage(ACLMessage.CONFIRM,answer,conversationID,reply);
			
			ToSendObject toSend= new ToSendObject();
			toSend.setConversId(conversationID);
			toSend.setEnergy(answer);
			toSend.setPerformative(ACLMessage.CONFIRM);
			toSend.setReply(reply);
			
			return toSend;
			}else
			{	
				if(msg.getPerformative()==ACLMessage.AGREE){
					
					getContentManager().registerLanguage(codec);
				    getContentManager().registerOntology(ontology);
					ContentElement content=null;
					try {
						content = getContentManager().extractContent(msg);
						Energy mo = (Energy)((Action)content).getAction();
						energy.setQuantity(energy.getQuantity()-mo.getQuantity());
						BrokerAgent.money=BrokerAgent.money+mo.getPrice();
						System.out.println(mo.getPrice()+"  "+mo.getQuantity());
						logger.info("money broker after sail electrycity to "+ conversationID+"=" +BrokerAgent.money);
						System.out.println("money broker after sail electrycity to "+ conversationID+"=" +BrokerAgent.money);
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
					
				}
				
			}
			
		}
		return null;
	}

}
