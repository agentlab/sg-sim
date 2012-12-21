package CsAgentForWB;

import jade.core.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import Ontologies.*;
import Behaviours.TestAgentBehaviour;

public class TestAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 423062763382794853L;
	private Codec codec = new SLCodec();
	private Ontology ontology = AgentForWbMessageOntology.getInstance();

	public void setup() {    
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			System.out.println("TestAgent.setup()");
			
			this.addBehaviour(new TestAgentBehaviour(this));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	    
	}  
}
