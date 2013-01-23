package sg_sim;

import ontologies.PowerChangeRequest;
import ontologies.PowerChangeRequestPredicate;
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
import jade.proto.AchieveREResponder;
import behaviours.*;

public class ControlAgent extends Agent {
    private static final long serialVersionUID = 2L;
    
    private Codec codec = new SLCodec(); 
 	private Ontology ontology = WindTurbineOntology.getInstance();    
	protected final String name = "YourTestAgentServiceName"; // agent service name
	protected final String type = "YourTestAgentServiceType"; // agent service type
	
    private int Power = 1;
    private int MaxPower = 10;
    
    public ACLMessage createEmptyMessage(AID reciever) {
        ACLMessage message = new ACLMessage(ACLMessage.NOT_UNDERSTOOD);

        // set language and ontology
        message.setLanguage(codec.getName());
		message.setOntology(ontology.getName());
        
        //set the receiver
        message.addReceiver(reciever);
    
        //set the timeout to 10 seconds
        long timeout = System.currentTimeMillis() + 10000;
        message.setReplyByDate(new java.util.Date(timeout));

        //sending a REQUEST
        message.setPerformative(ACLMessage.REQUEST);
        message.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
      
        System.out.println(getLocalName() + " is sending a " + ACLMessage.getPerformative(message.getPerformative()) + " message to initiate the protocol");
        return message;
    }
    
    public ACLMessage createPowerChangeRequest(int power) {
    	AID reciever = new AID("WindTurbineAgent", false);
    	ACLMessage message = createEmptyMessage(reciever);
    	PowerChangeRequest powerChangeRequest = new PowerChangeRequest(power);
    	PowerChangeRequestPredicate predicate = new PowerChangeRequestPredicate();
    	predicate.setPowerChangeRequest(powerChangeRequest);
    	
    	try {
    		getContentManager().fillContent(message, predicate);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return message;
    }
    
    public void setup() {
    	getContentManager().registerLanguage(codec);
		getContentManager().registerOntology(ontology);  

    	Behaviour requestB = new ControlAgentRespondRequestBehaviour(this, AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST));
        addBehaviour(requestB);
        
        // subscription
        // Register the service in the yellow pages
     	ServiceDescription sd = new ServiceDescription();
     	sd.setType(type);
     	sd.setName(name);

     	DFAgentDescription dfd = new DFAgentDescription();
     	dfd.setName(getAID());
     	dfd.addServices(sd);
     	try {
     		DFService.register(this, dfd);
     	} catch (FIPAException fe) {
     		fe.printStackTrace();
     	}

     	subscribe();
    }
    
    protected void subscribe() {
		ServiceDescription sd = new ServiceDescription();
		sd.setName("WindTurbine");
		sd.setType("Power");

		System.out.println("It's subscribes all the way down");

		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);

		addBehaviour(new RegisterSubscriptionBehaviour(template, this, 1000));
	}
    
    public void setPower(int newPower) {
    	Power = newPower;
    }
    
    public int getPower() {
    	return Power;
    }
    
    public int getMaxPower() {
    	return MaxPower;
    }
}
