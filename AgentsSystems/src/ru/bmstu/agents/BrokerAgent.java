package ru.bmstu.agents;

import java.io.IOException;
import java.util.Random;

import org.apache.log4j.Logger;

import ru.bmstu.objectToSell.Electricity;
import ru.bmstu.ontology.Energy;
import ru.bmstu.ontology.EnergyOntology;
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
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;


/**
 * ВАЖНАЯ ССЫЛКА !!!! http://jade.tilab.com/doc/tutorials/JADEAdmin/JadePlatformTutorial.html
 * @author Admin
 *
 */
public class BrokerAgent extends Agent {
	private static final Logger logger = Logger.getLogger(BrokerAgent.class);
	private static final long serialVersionUID = 8257866411543354395L;
	public static int money=15000;
	private Codec codec = new SLCodec();
	private Ontology ontology = EnergyOntology.getInstance();
	
	public void sendMessage(Agent myAgent, int performative, AgentAction action,String conversId,ACLMessage _msg) 
	{	

		DFAgentDescription[] agents = null;
		AID agentID=null;
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults(new Long(-1));
			agents = DFService.search(myAgent, new DFAgentDescription(), c);
		} catch (Exception e) {
			System.out.println("Problem searching AMS: " + e);
			e.printStackTrace();
		}
		for (DFAgentDescription agent : agents) {
			//if(agent.getName().getLocalName().equals("babushka"))
			{
				agentID = agent.getName();
			}
		}
		

		ACLMessage msg = new ACLMessage(performative);
		if(_msg!=null)
		{
			msg=_msg;
		}
		msg.setPerformative(performative);
		msg.setLanguage(codec.getName());
		msg.setOntology(ontology.getName());
		msg.setConversationId(conversId);
		 try {
			 
			 getContentManager().fillContent(msg, new Action(agentID, action));
		 	send(msg);
		 }catch (Exception e){
			 e.printStackTrace();
		 }
	}
	public void setup() {
		getContentManager().registerLanguage(codec);
	    getContentManager().registerOntology(ontology);
		System.out.println("Hello World, my name is : " + getAID().getName());
		
		DFAgentDescription dfd = new DFAgentDescription();
		   dfd.setName(getAID());
		   ServiceDescription sd = new ServiceDescription();
		   sd.setType("BrokerAgent");
		   sd.setName("BrokerAgent");
		   dfd.addServices(sd);
		   try {
		    DFService.register(this, dfd);
		   }
		   catch (FIPAException fe) {
		    fe.printStackTrace();
		   }
		
		
		
		// Поведение агента исполняемое в цикле
		addBehaviour(new CyclicBehaviour(this) {
			BrokerReplyer broker = new BrokerReplyer();
			boolean canSetNewTime=true;
			long lastTime=0;
			
			int quantityForBuy=0;
			int priceForBuy=0;
			Electricity energy=new Electricity(0, 0);
			private static final long serialVersionUID = 7774831398907094833L;
			public void action() {
				if(canSetNewTime)
				{
					lastTime=System.currentTimeMillis()+test.BrokerChangePrice;
					canSetNewTime=false;
				}
				
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
				
				
				/*
				MessageTemplate mt =MessageTemplate.MatchConversationId("Babushka");
				ACLMessage msg = myAgent.receive(mt);
				if (msg != null) 
				{ 
					if(msg.getPerformative()==ACLMessage.REQUEST)
					{
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
				          System.out.println("neeeeeed quaaa"+mo.getNeedToBuy());
					}
				}
				
				
				
				ToSendObject toSend=broker.messageListener(myAgent, energy, money, "Babushka");
				if(toSend!=null)
				sendMessage(myAgent, toSend.getPerformative(), toSend.getEnergy(), toSend.getConversId(), toSend.getReply());
			//	if(msg!=null)
				//{send(msg);}
				
				ToSendObject toSend1=broker.messageListener(myAgent, energy, money, "Autokolonna");
				if(toSend1!=null)
				sendMessage(myAgent, toSend1.getPerformative(), toSend1.getEnergy(), toSend1.getConversId(), toSend1.getReply());
				
				ToSendObject toSend2=broker.messageListener(myAgent, energy, money, "Driver");
				if(toSend2!=null)
				sendMessage(myAgent, toSend2.getPerformative(), toSend2.getEnergy(), toSend2.getConversId(), toSend2.getReply());
				/*
				ACLMessage DriverRply=broker.messageListener(myAgent, energy, money, "Driver");
				if(DriverRply!=null)
					send(DriverRply);
				
				
				
				ACLMessage AutokolRply=broker.messageListener(myAgent, energy, money, "Autokolonna");
				if(AutokolRply!=null)
					send(AutokolRply);
				*/
				
				/*
				MessageTemplate mt =MessageTemplate.MatchConversationId("Babushka");
				ACLMessage msg = myAgent.receive(mt);
				if (msg != null) 
				{ 
					if(msg.getPerformative()==ACLMessage.REQUEST){
					String quantity_to_sail= msg.getContent();
					System.out.println("tekushaya cena "+energy.getPrice());
					long priceForNeedQuantity=Integer.parseInt(quantity_to_sail)*(energy.getPrice()+1);
					System.out.println("cena dlya babushki "+(energy.getPrice()+1));
					ACLMessage reply = msg.createReply();
					reply.setConversationId("Babushka");
					try {
						reply.setContentObject(new Electricity(priceForNeedQuantity,Long.parseLong(quantity_to_sail)));
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					reply.setPerformative(ACLMessage.CONFIRM);
					send(reply);
					}else
					{	
						if(msg.getPerformative()==ACLMessage.AGREE){
							try {1
								energy.setQuantity(energy.getQuantity()-((Electricity)msg.getContentObject()).getQuantity());
								money=money+((Electricity)msg.getContentObject()).getPrice();
							} catch (UnreadableException e) {
								e.printStackTrace();
							}
							logger.debug("money broker after sail electrycity ++= "+money);
							System.out.println("money broker after sail electrycity ++= "+money);
						}
					}
				}
				
				
				
				*/
				
				/*else{
				// Блокируем поведение, пока в очереди сообщений агента
				// не появится хотя бы одно сообщение
			block();}*/
			}
		});

	}
}
