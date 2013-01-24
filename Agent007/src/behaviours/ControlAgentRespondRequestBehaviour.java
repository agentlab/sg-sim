package behaviours;

import jade.content.ContentElement;
import jade.core.behaviours.Behaviour;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.*;

import sg_sim.ControlAgent;
import ontologies.*;

public class ControlAgentRespondRequestBehaviour extends AchieveREResponder {
	private static final long serialVersionUID = 4L;

	private ControlAgent controlAgent;
	
    public ControlAgentRespondRequestBehaviour(ControlAgent agent, MessageTemplate mt) {
      super(agent, mt);
      controlAgent = agent;
    }
  
    protected ACLMessage prepareResponse(ACLMessage request) throws RefuseException, NotUnderstoodException {
        ACLMessage reply = request.createReply();

    	try {
    		ContentElement content = myAgent.getContentManager().extractContent(request);
            PowerRequestPredicate powerRequestPredicate = (PowerRequestPredicate)content;
            PowerRequest powerRequest = powerRequestPredicate.getPowerRequest();
            
            if (controlAgent.getMaxPower() >= powerRequest.getPower()) {
          	  	reply.setPerformative(ACLMessage.AGREE);
          	  	
          	  	// change power
          	    ACLMessage powerChangeRequest = controlAgent.createPowerChangeRequest(powerRequest.getPower());
          	    Behaviour changePowerBehaviour =
          	    		new ControlAgentRequestBehaviour(myAgent, powerChangeRequest);
          	    myAgent.addBehaviour(changePowerBehaviour);
          	    
          	    // turn the WindTurbine off
          	    ACLMessage turnOffRequest = controlAgent.createPowerChangeRequest(0);
          	    Behaviour turnOffBehaviour =
          	    		new ControlAgentRequestBehaviour(myAgent, turnOffRequest);
          	    myAgent.addBehaviour(
          	    		new TurnOffBehaviour(myAgent, powerRequest.getTimeInterval(), turnOffBehaviour));
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
  } // End of class ControlAgentRespondRequestBehaviour