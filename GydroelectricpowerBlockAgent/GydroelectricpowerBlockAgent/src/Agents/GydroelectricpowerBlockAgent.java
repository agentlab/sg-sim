package Agents;

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

public class GydroelectricpowerBlockAgent extends Agent {

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
	private Ontology ontology = GydroelectricpowerBlockAgentOntology.getInstance();
	private int consumption=250;
	private double perfomance=0.75;
	private int pressure=38;
	private double constg=9.81;
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

			if (this.consumption==0) ;
			else System.out.println("No water");
			addBehaviour(dfSubscriptionResponder);
			
			this.addBehaviour(new GydroelectricpowerBlockAgentBehaviour(this, consumption, perfomance, pressure, constg, subManager, dfSubscriptionResponder));
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
