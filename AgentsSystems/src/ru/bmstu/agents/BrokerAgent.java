package ru.bmstu.agents;

import java.io.IOException;
import java.util.Random;

import org.apache.log4j.Logger;

import ru.bmstu.objectToSell.Electricity;
import ru.bmstu.ontology.Energy;
import ru.bmstu.ontology.EnergyOntology;
import ru.bmstu.protocols.BrokeringInitiator;
import ru.bmstu.protocols.BrokeringResponder;
import ru.bmstu.simpleObj.ToSendObject;

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
import jade.core.behaviours.TickerBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * ¬ј∆Ќјя ——џЋ ј !!!! http://jade.tilab.com/doc/tutorials/JADEAdmin/JadePlatformTutorial.html
 * @author Admin
 *
 */
public class BrokerAgent extends Agent {
	private static final Logger logger = Logger.getLogger(BrokerAgent.class);
	private static final long serialVersionUID = 8257866411543354395L;
	public static int money=15000;
	private Codec codec = new SLCodec();
	private Ontology ontology = EnergyOntology.getInstance();
	
	boolean canSetNewTime=true;
	long lastTime=0;
	
	int quantityForBuy=0;
	int priceForBuy=0;
	Electricity energy=new Electricity(0, 0);


	public void setup() {
		getContentManager().registerLanguage(codec);
	    getContentManager().registerOntology(ontology);
		System.out.println("Hello World, my name is : " + getAID().getName());
		
		
		addBehaviour(new TickerBehaviour(this,new Long(2000))
		{
			
			@Override
			protected void onTick() {//провер€ет сколько энергии осталось и докупает ее и подводит итоги
				
				if((energy.getQuantity()<=110))
				{	
					System.out.println("ALL BROKER MONEY AFTER SAIL "+(money+energy.getPrice()*energy.getQuantity()));
					Random r = new Random();
					int price =1+ r.nextInt(7);//цена за 1 квт
					int quantity=0;
					if(energy==null)
					{
						quantity=2000;
						money=money-price*quantity;
					}else
					{
						quantityForBuy=2000-energy.getQuantity();
						priceForBuy=price*quantityForBuy;
					}
					energy= new Electricity(price, energy.getQuantity()+quantityForBuy);
					money=money-priceForBuy;
					logger.info("electricity need to buy "+quantityForBuy+"; current price of electr = "+price+"; money of broker after buy electr = "+money);
					System.out.println("electricity need to buy "+quantityForBuy+";current price of electr = "+price+"; money of broker after buy electr = "+money);
					canSetNewTime=true;
				}
				
			}
		});
		
		addBehaviour(new BrokeringResponder(this) {

			public ACLMessage handleRequest(ACLMessage msg) {
						
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
					System.out.println(msg.getConversationId()+" hochet kupit "+quantity_to_sail);
					System.out.println("tekushaya cena"+ msg.getConversationId() +" "+energy.getPrice());
					int priceForNeedQuantity=Integer.parseInt(quantity_to_sail)*(energy.getPrice()+1);
					System.out.println("cena dlya "+msg.getConversationId()+" "+(energy.getPrice()+1));
					
					ACLMessage reply = msg.createReply();
					
					Energy answer= new Energy();
					//answer.setNeedToBuy("ddsd");
					answer.setPrice(priceForNeedQuantity);
					answer.setQuantity(Integer.parseInt(quantity_to_sail));
					 try {
						getContentManager().fillContent(reply, answer);
					} catch (CodecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 return reply;
				
			}
			
			public void handleAgree(ACLMessage msg) 
			{
				ContentElement content=null;
				try {
					content = getContentManager().extractContent(msg);
					Energy mo = (Energy)((Action)content).getAction();
					energy.setQuantity(energy.getQuantity()-mo.getQuantity());
					BrokerAgent.money=BrokerAgent.money+mo.getPrice();
					System.out.println(mo.getPrice()+"  "+mo.getQuantity());
					logger.info("money broker after sail electrycity to "+ msg.getConversationId()+"=" +BrokerAgent.money);
					System.out.println("money broker after sail electrycity to "+ msg.getConversationId()+"=" +BrokerAgent.money);
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
		});
		

	}
}
