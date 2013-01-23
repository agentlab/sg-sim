package behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.*;

public class ProducerAgentRequestBehaviour extends AchieveREInitiator {
    private static final long serialVersionUID = 1L;
	
	public ProducerAgentRequestBehaviour(Agent a, ACLMessage req) {
      super(a, req);
    }
  
    protected void handleAgree(ACLMessage agree) {
      System.out.println("ProducerAgent in handleAgree: " + agree);
    }
  
    protected void handleRefuse(ACLMessage refuse) {
      System.out.println("ProducerAgent in handleRefuse: " + refuse);
    }
    protected void handleFailure(ACLMessage failure) {
      System.out.println("ProducerAgent in handleFailure: " + failure);
    }
    protected void handleNotUnderstood(ACLMessage notUnderstood) {
      System.out.println("ProducerAgent in handleNotUnderstood: " + notUnderstood);
    }
    protected void handleInform(ACLMessage inform) {
      System.out.println("ProducerAgent in handleInform: " + inform);
    }
}
