package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementVocabulary;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

/**
 * Agent subscribes to other agent and receives subscription data
 * Has TWO behaviours: RegisterBehaviour and StubscriptionInitiator
 * 
 */
public class SubscrAgent extends Agent {
	private static final long serialVersionUID = -2961241167339514488L;
	protected final String name = "YourTestAgentServiceName"; // agent service name
	protected final String type = "YourTestAgentServiceType"; // agent service type

	/**
	 * agent initializations
	 * 
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected void setup() {
		
		System.out.println(getAID().getName() + " started.");

		// ����������� �� ������� ������ �������
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

		// Printout a welcome message
		System.out.println("Agent " + getAID().getName() + " is ready.");
	}

	protected void subscribe() {
		ServiceDescription sd = new ServiceDescription();
		sd.setName("Waterbattery");
		sd.setType("Water");
		
		System.out.println("It's subscribes all the way down");

		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);
		
		addBehaviour(new testbehaviour.RegisterBehaviour(template, this, 1000));
	}

	/**
	 * Put agent clean-up operations here
	 * 
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		System.out.println("Agent " + getLocalName() + " is shutting down.");
	}
}