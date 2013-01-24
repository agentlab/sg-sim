package behaviours;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class ControlAgentRequestBehaviour extends AchieveREInitiator {
    private static final long serialVersionUID = 1L;
	
    public ControlAgentRequestBehaviour(Agent a, ACLMessage req) {
      super(a, req);
    }
  
    protected void handleAgree(ACLMessage agree) {
      System.out.println("ControlAgent in handleAgree: " + agree);
    }
  
    protected void handleRefuse(ACLMessage refuse) {
      System.out.println("ControlAgent in handleRefuse: " + refuse);
    }
    protected void handleFailure(ACLMessage failure) {
      System.out.println("ControlAgent in handleFailure: " + failure);
    }
    protected void handleNotUnderstood(ACLMessage notUnderstood) {
      System.out.println("ControlAgent in handleNotUnderstood: " + notUnderstood);
    }
    protected void handleInform(ACLMessage inform) {
      System.out.println("ControlAgent in handleInform: " + inform);
    }
}
