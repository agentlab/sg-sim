package agents;

import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.ContractNetResponder;

public class Broker extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7779532957753246851L;
	public String price;
	public String volume;
	public String Evalue;
	protected void setup() {
		System.out.println("Agent "+getLocalName()+" waiting for CFP...");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			price =  (String) args[0];
			volume=  (String) args[1];
			
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.setName(getAID());
			ServiceDescription sd = new ServiceDescription();
			sd.setType("energy-selling");
			sd.setName("Energy-Broker");
			dfd.addServices(sd);
			try {
				DFService.register(this, dfd);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}			
		}
		else{
			System.out.println(getAID().getName()+" No available arguments");
			doDelete();
		}
		
		MessageTemplate template = MessageTemplate.and(
				MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_CONTRACT_NET),
				MessageTemplate.MatchPerformative(ACLMessage.CFP) );

		addBehaviour(new ContractNetResponder(this, template) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -8436872907122692247L;
			//private Broker agent=(Broker)myAgent;
			
			
			@Override
			protected ACLMessage handleCfp(ACLMessage cfp) throws NotUnderstoodException, RefuseException {
				InformMessage a;
				System.out.println("Agent "+getLocalName()+": CFP received from "+cfp.getSender().getName()+". Action is "+cfp.getConversationId());
				try {
					a = (InformMessage)getContentManager().extractContent(cfp);
					Evalue=String.valueOf(a.getVolume());
				} catch (UngroundedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (CodecException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (OntologyException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (Integer.parseInt(Evalue)<Integer.parseInt(volume)) {
					// We provide a proposal
					System.out.println("Agent "+getLocalName()+": Proposing "+price);
					ACLMessage propose = cfp.createReply();
					propose.setOntology(RequestOntology.getInstance().getName());
					InformMessage imessage = new InformMessage();
					imessage.setPrice(Integer.parseInt(price));
					imessage.setVolume(Integer.parseInt(volume));
					propose.setPerformative(ACLMessage.PROPOSE);
					propose.setContent(price);
					try {
						myAgent.getContentManager().fillContent(propose, imessage);
					} catch (CodecException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OntologyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					return propose;
				}
				else {
					// We refuse to provide a proposal
					System.out.println("Agent "+getLocalName()+": Refuse");
					throw new RefuseException("evaluation-failed");
				}
			}

			@Override
			protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose,ACLMessage accept) throws FailureException {
				System.out.println("Agent "+getLocalName()+": Proposal accepted");
				
					System.out.println("Agent "+getLocalName()+": Action successfully performed");
					ACLMessage inform = accept.createReply();
					inform.setPerformative(ACLMessage.INFORM);
					return inform;
				
			}

			protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
				System.out.println("Agent "+getLocalName()+": Proposal rejected");
			}
		} );
	}
	
}
