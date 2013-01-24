package sg_sim;

import ontologies.WindTurbineOntology;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.*;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;
import jade.proto.SubscriptionResponder;
import behaviours.StateSubscriptionManager;
import behaviours.WindTurbineAgentRespondRequestBehaviour;
import behaviours.WindTurbineSubscriptionBehaviour;

public class WindTurbineAgent extends Agent {
    private static final long serialVersionUID = 1L;

	protected final String name = "WindTurbine"; // agent service name
	protected final String type = "Power"; // agent service type
    
    private int power = 1;
    private int maxPower = 10;
        
    private Codec codec = new SLCodec(); 
 	private Ontology ontology = WindTurbineOntology.getInstance();    
 	private behaviours.StateSubscriptionManager subManager;
	private SubscriptionResponder dfSubscriptionResponder;
 	
    public void setup() {
    	// respond to Control
    	getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);  

    	Behaviour requestB = new WindTurbineAgentRespondRequestBehaviour(this, AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST));
        addBehaviour(requestB);
        
        // subscription
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(name);
		sd.addOntologies("Wind_Turbine_Ontology");
		dfd.addServices(sd);
		
		subManager = new StateSubscriptionManager(this);
		MessageTemplate mt = SubscriptionResponder.createMessageTemplate(ACLMessage.SUBSCRIBE);
	
		// Behaviour dealing with subscriptions
		dfSubscriptionResponder = new SubscriptionResponder(this, mt, subManager);
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			addBehaviour(dfSubscriptionResponder);

			this.addBehaviour(new WindTurbineSubscriptionBehaviour(this, power, maxPower, subManager));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
    }
    
    public void setPower(int newPower) {
    	power = newPower;
    }
    
    public int getPower() {
    	return power;
    }
    
    public int getMaxPower() {
    	return maxPower;
    }
}
