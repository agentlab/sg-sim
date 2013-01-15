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

public class SolarAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	protected final String name = "SolarAgent"; // agent service name
	protected final String type = "Power"; // agent service type
	protected final String type2 = "Electricity"; // agent service type

	private behaviours.StateSubscriptionManager subManager;
	private SubscriptionResponder dfSubscriptionResponder;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = SolarAgentOntology.getInstance();
	
	private double insol = 21.0; //Insolation in January
	private double Wbat = 1000; //Nominal battery power
	private int insolMax = 1000; //Max insolation on m2
	private double mu = 0.91; //KPD
	
	public void setup() { 
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type); // your agent type
		sd.setName(name); // your agent name
		sd.addOntologies("SolarAgentOntology");
		dfd.addServices(sd);
		
		ServiceDescription sd2 = new ServiceDescription();
		sd2.setType(type2); // your agent type
		sd2.setName(name); // your agent name
		sd2.addOntologies("SolarAgentOntology");
		dfd.addServices(sd2);
		
		subManager = new behaviours.StateSubscriptionManager (this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);

		// Behaviour dealing with subscriptions
		dfSubscriptionResponder = new SubscriptionResponder(this, mt, subManager);
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			if (this.insol==0);
			else System.out.println("Polar night, sir!");
			addBehaviour(dfSubscriptionResponder);
			
			this.addBehaviour(new WindTurbBehaviour(this, insol, Wbat, insolMax, mu, subManager, dfSubscriptionResponder));
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
