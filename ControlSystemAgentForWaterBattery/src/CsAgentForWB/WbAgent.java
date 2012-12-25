package CsAgentForWB;

import Behaviours.*;
import Ontologies.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class WbAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private Codec codec = new SLCodec();
	private Ontology ontology = AgentForWbOntology.getInstance();

	public void setup() {    
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			System.out.println("WbAgent.setup()");
			MessageTemplate template = MessageTemplate.and(
					MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
	
			this.addBehaviour(new TestAgentResponderBehaviour(this, template));     
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	    
	} 

}
