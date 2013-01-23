package sg_sim;

import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.*;

import jade.domain.FIPANames;
import behaviours.*;
import ontologies.*;

public class ProducerAgent extends Agent {
    private static final long serialVersionUID = 2L;
    
    private Codec codec = new SLCodec(); 
	private Ontology ontology = WindTurbineOntology.getInstance();    
    
    ACLMessage createEmptyMessage(AID reciever) {
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
    
    ACLMessage createPowerRequest(int power, int timeInterval) {
    	AID reciever = new AID("ControlAgent", false);
    	ACLMessage message = createEmptyMessage(reciever);
    	PowerRequest powerRequest = new PowerRequest(power, timeInterval);
    	PowerRequestPredicate predicate = new PowerRequestPredicate();
    	predicate.setPowerRequest(powerRequest);
    	
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
	  
        ACLMessage powerRequest = createPowerRequest(100, 3000);
    
        Behaviour b = new ProducerAgentRequestBehaviour(this, powerRequest);
        addBehaviour(b);
    }
   
} // end class ProducerAgent