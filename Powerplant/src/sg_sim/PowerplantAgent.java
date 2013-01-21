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

public class PowerplantAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected final String name = "Powerplant"; // agent service name
	protected final String type = "Power"; // agent service type
	protected final String type2 = "Electricity"; // agent service type

	private behaviours.StateSubscriptionManager subManager;
	private SubscriptionResponder dfSubscriptionResponder;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = PowerplantOntology.getInstance();
	private int powerMass=10000;//Í„
	private double spheat=29;
	private double efficiency2=0.37;
	private double efficiency1=0.7;
	public void setup() { 
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type); // your agent type
		sd.setName(name); // your agent name
		sd.addOntologies("Power_plant_Ontology");
		dfd.addServices(sd);
		
		ServiceDescription sd2 = new ServiceDescription();
		sd2.setType(type2); // your agent type
		sd2.setName(name); // your agent name
		sd2.addOntologies("Power_plant_Ontology");
		dfd.addServices(sd2);
		
		subManager = new behaviours.StateSubscriptionManager (this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);

		// Behaviour dealing with subscriptions
		dfSubscriptionResponder = new SubscriptionResponder(this, mt, subManager);
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			if (this.powerMass==0) ;
			else System.out.println("no fuel");
			addBehaviour(dfSubscriptionResponder);
			
			this.addBehaviour(new PowerplantBehaviour(this, powerMass, spheat, efficiency2, efficiency1, subManager, dfSubscriptionResponder));
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
