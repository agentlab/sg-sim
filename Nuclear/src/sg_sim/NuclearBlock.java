package sg_sim;

import behaviours.*;
import ontologies.*;
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

public class NuclearBlock extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected final String name = "TURBINE"; // agent service name
	protected final String type = "ASU"; // agent service type
	protected final String type2 = "TRANS"; // agent service type

	private behaviours.StateSubscriptionManager subManager;
	private SubscriptionResponder dfSubscriptionResponder;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = NuclearPowerPlantBlockOntology.getInstance();
	private int fuel=12;
	private double p1=1.225;
	private int p2=52;
	private double bLimit=0.4;
	public void setup() { 
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type); // your agent type
		sd.setName(name); // your agent name
		sd.addOntologies("NuclearPowerPlantBlockOntology");
		dfd.addServices(sd);
		
		ServiceDescription sd2 = new ServiceDescription();
		sd2.setType(type2); // your agent type
		sd2.setName(name); // your agent name
		sd2.addOntologies("NuclearPowerPlantBlockOntology");
		dfd.addServices(sd2);
		// 
		subManager = new behaviours.StateSubscriptionManager (this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);

		// Behaviour dealing with subscriptions
		dfSubscriptionResponder = new SubscriptionResponder(this, mt, subManager);
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			if (this.fuel==0) ;
			else System.out.println("");
			addBehaviour(dfSubscriptionResponder);
			
			this.addBehaviour(new NuclearPowerPlantAgentBehaviour(this, fuel, p1, p2, bLimit, subManager, dfSubscriptionResponder));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

	}

}
