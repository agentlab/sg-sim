package sg_sim;

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

public class WindTurbAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected final String name = "WindTurbine"; // agent service name
	protected final String type = "Power"; // agent service type
	protected final String type2 = "Electricity"; // agent service type

	private behaviours.StateSubscriptionManager subManager;
	private SubscriptionResponder dfSubscriptionResponder;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = WindTurbOntology.getInstance();
	private int windSpeed=12;
	private double airDensity=1.225;
	private int length=52;
	private double bLimit=0.4;
	public void setup() { 
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type); // your agent type
		sd.setName(name); // your agent name
		sd.addOntologies("Wind_Turbine_Ontology");
		dfd.addServices(sd);
		
		ServiceDescription sd2 = new ServiceDescription();
		sd2.setType(type2); // your agent type
		sd2.setName(name); // your agent name
		sd2.addOntologies("Wind_Turbine_Ontology");
		dfd.addServices(sd2);
		
		subManager = new behaviours.StateSubscriptionManager (this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);

		// Behaviour dealing with subscriptions
		dfSubscriptionResponder = new SubscriptionResponder(this, mt, subManager);
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			if (this.windSpeed==0) ;
			else System.out.println("Shhhhh...");
			addBehaviour(dfSubscriptionResponder);
			
			this.addBehaviour(new WindTurbBehaviour(this, windSpeed, airDensity, length, bLimit, subManager, dfSubscriptionResponder));
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
