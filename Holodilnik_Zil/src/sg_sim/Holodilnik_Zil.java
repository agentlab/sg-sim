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

public class Holodilnik_Zil extends Agent
{

	private static final long serialVersionUID = 1L;
	

	protected final String name = "Holodilnik_Zil"; // agent service name
	protected final String type = "Cooling"; // agent service type
	protected final String type2 = "Electricity"; // agent service type


	private behaviours.StateSubscriptionManager subManager;
	private SubscriptionResponder dfSubscriptionResponder;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = HolodilnikOntology.getInstance();
	private double max_temp				= 25.0;
	private double min_temp				= -5.0;
	private double chill_delta 					= 0.5;
	private double W_hh 						= 5;
	private double W_on 						= 10;
	private double W_off 						= 1;
	public void setup() { 
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType(type); // your agent type
		sd.setName(name); // your agent name
		sd.addOntologies("Holodilnik_Ontology");
		dfd.addServices(sd);
		
		ServiceDescription sd2 = new ServiceDescription();
		sd2.setType(type2); // your agent type
		sd2.setName(name); // your agent name
		sd2.addOntologies("Holodilnik_Ontology");
		dfd.addServices(sd2);		

		
		subManager = new behaviours.StateSubscriptionManager (this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);

		// Behaviour dealing with subscriptions
		dfSubscriptionResponder = new SubscriptionResponder(this, mt, subManager);
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			
			addBehaviour(dfSubscriptionResponder);
			
			this.addBehaviour(new HolodilnikBehaviour(this, max_temp, min_temp, chill_delta, W_hh, W_on, W_off, subManager, dfSubscriptionResponder));
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
