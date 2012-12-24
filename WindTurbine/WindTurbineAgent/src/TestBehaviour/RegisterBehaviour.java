package TestBehaviour;

import sg_sim.SubscrAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FIPAManagementVocabulary;
import jade.lang.acl.ACLMessage;

/**
 * This behaviour searches for first agent which matches template.
 * Then it starts SubscrInitiatorBehaviour.
 * Then it cancels itself if all done
 * 
 */
public class RegisterBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 3384651319762649616L;
	
	protected DFAgentDescription template;

	/**
	 * Constructor
	 * 
	 * @param template - agent description for search and subscription
	 * @param a - agent
	 * @param period - repeat period in milliseconds
	 */
	public RegisterBehaviour(DFAgentDescription template, Agent a, long period) {
		super(a, period);
		this.template = template;
	}

	/**
	 * @see jade.core.behaviours.TickerBehaviour#onTick()
	 */
	@Override
	protected void onTick() {
		// get the list of agents according template
		DFAgentDescription[] result;
		try {
			result = DFService.search(myAgent, template);
			System.out.println("Subscriber found " + result.length + " agents.");
			if (result.length > 0) {
				AID subscriptionAgent = result[0].getName();

				ACLMessage subscribe = createRequestMessage(myAgent, subscriptionAgent);
				subscribe.setPerformative(ACLMessage.SUBSCRIBE);
				subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
				// Note that iota is not included in SL0
				subscribe.setLanguage(FIPANames.ContentLanguage.FIPA_SL);
				
				// create StubscriptionInitiator behaviour
				myAgent.addBehaviour(new SubscrInitiatorBehaviour((SubscrAgent)myAgent, subscribe));
				
				// cancel this behaviour and remove it from agent
				stop();
			}
		} catch (FIPAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * create a REQUEST message with the following slots:
	 * <code> (REQUEST :sender sender.getAID()
	 * :receiver receiver
	 * :protocol fipa-request
	 * :language FIPA-SL0
	 * :ontology fipa-agent-management
	 * :reply-with xxx
	 * :conversation-id xxx) </code>
	 * where
	 * <code>xxx</code> are unique words generated on the basis of the
	 * sender's name and the current time.
	 * 
	 * @param sender
	 *            is the Agent sending the message
	 * @param receiver
	 *            is the AID of the receiver agent
	 * @return an ACLMessage object
	 */
	protected static ACLMessage createRequestMessage(Agent sender, AID receiver) {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setSender(sender.getAID());
		request.addReceiver(receiver);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.setLanguage(FIPANames.ContentLanguage.FIPA_SL0);
		request.setOntology(FIPAManagementVocabulary.NAME);
		request.setReplyWith("rw" + sender.getName() + System.currentTimeMillis());
		request.setConversationId("0conv" + sender.getName() + System.currentTimeMillis());
		return request;
	}

}
