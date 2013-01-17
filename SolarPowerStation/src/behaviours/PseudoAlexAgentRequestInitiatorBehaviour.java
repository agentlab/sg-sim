package behaviours;
import java.util.Vector;
import jade.proto.AchieveREInitiator;
import jade.core.Agent;
import jade.lang.acl.ACLMessage;


public class  PseudoAlexAgentRequestInitiatorBehaviour extends AchieveREInitiator {

	private static final long serialVersionUID = 1L;


	public PseudoAlexAgentRequestInitiatorBehaviour(Agent a, ACLMessage msg) {
		super(a, msg);
		// TODO Auto-generated constructor stub
	}

	protected void handleInform(ACLMessage inform) {
		System.out.println("PseudoAlexAgent recieved INFORM from " + inform.getSender().getName());
	}

	protected void handleRefuse(ACLMessage refuse) {
		System.out.println("PseudoAlexAgent recieved REFUSE from " + refuse.getSender().getName());
	}

	protected void handleFailure(ACLMessage failure) {
		if(failure.getSender().equals(myAgent.getAMS())) {
			System.out.println("Failure delivering message");
		} else {
			System.out.println("PseudoAlexAgent recieved FAILURE from " + failure.getSender().getName());
		}
	}

	protected void handleAllResultNotifications(Vector notifications) {
		System.out.println("hARN");
	}
}

