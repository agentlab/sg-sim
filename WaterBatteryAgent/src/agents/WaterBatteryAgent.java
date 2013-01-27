package agents;

import behaviours.*;
import ontologies.*;
import behaviours.StateSubscriptionManager;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;

public class WaterBatteryAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected final String name = "Waterbattery"; // agent service name
	protected final String type = "Water"; // agent service type
	protected final String type2 = "Electricity"; // agent service type

	private behaviours.StateSubscriptionManager subManager;
	private SubscriptionResponder dfSubscriptionResponder;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = WaterBatteryOntology.getInstance();
	private int waterMass=10000;//масса воды,падающей на турбину
	private double spheat=29;
	private double efficiency2=0.37;
	private double efficiency1=0.7;
	public void setup() { 
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type); // тип нашего агента
		sd.setName(name); // имя нашего агента
		sd.addOntologies("Water_battery_Ontology");
		dfd.addServices(sd);
		
		ServiceDescription sd2 = new ServiceDescription();
		sd2.setType(type2); // your agent type
		sd2.setName(name); // your agent name
		sd2.addOntologies("Water_battery_Ontology");
		dfd.addServices(sd2);
		
		subManager = new behaviours.StateSubscriptionManager (this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);

		// Behaviour dealing with subscriptions
		dfSubscriptionResponder = new SubscriptionResponder(this, mt, subManager);
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			if (this.waterMass==0) ;
			else System.out.println("no water");
			addBehaviour(dfSubscriptionResponder);
			
			this.addBehaviour(new WaterBatteryBehaviour(this, waterMass, spheat, efficiency2, efficiency1, subManager, dfSubscriptionResponder));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

		// Initiate the SubscriptionManager used by the DF
		

		// message template for subscription
		
	}

}
