package behaviours;

import jade.content.ContentElement;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.*;

import sg_sim.WindTurbineAgent;
import ontologies.*;

public class WindTurbineAgentRespondRequestBehaviour extends AchieveREResponder {
	private static final long serialVersionUID = 4L;

	private WindTurbineAgent windTurbineAgent;
	
    public WindTurbineAgentRespondRequestBehaviour(WindTurbineAgent agent, MessageTemplate mt) {
        super(agent, mt);
        windTurbineAgent = agent;
    }
  
    protected ACLMessage prepareResponse(ACLMessage request) throws RefuseException, NotUnderstoodException {
        ACLMessage reply = request.createReply();

    	try {
    		ContentElement content = myAgent.getContentManager().extractContent(request);
            PowerChangeRequestPredicate powerChangeRequestPredicate = (PowerChangeRequestPredicate)content;
            PowerChangeRequest powerChangeRequest = powerChangeRequestPredicate.getPowerChangeRequest();
            
            if (windTurbineAgent.getMaxPower() >= powerChangeRequest.getPower()) {
            	windTurbineAgent.setPower(powerChangeRequest.getPower());
            	reply.setPerformative(ACLMessage.AGREE);
                System.out.println("Power changed. New value: " + powerChangeRequest.getPower());
            } else {
            	reply.setPerformative(ACLMessage.REFUSE);
    		}            
            
            System.out.println( myAgent.getLocalName() + " --> is sending "+(reply==null?"no":(reply.getPerformative()==ACLMessage.SUBSCRIBE?"an out-of-sequence":ACLMessage.getPerformative(reply.getPerformative())))+ " response to the protocol initiator." );
        } catch (Exception e) {
        	e.printStackTrace();
        }

    	return reply;  
    }
  
    protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) throws FailureException {
      ACLMessage resNot = request.createReply();
      
      resNot.setPerformative(ACLMessage.INFORM);
      System.out.println( myAgent.getLocalName() +  " --> is sending "+(resNot==null?"no":(resNot.getPerformative()==ACLMessage.SUBSCRIBE?"an out-of-sequence":ACLMessage.getPerformative(resNot.getPerformative())))+ " result notification to the protocol initiator." );
      return resNot;
    }
} // End of class WindTurbineAgentRespondRequestBehaviour