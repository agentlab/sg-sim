package CsAgentForWB;

import jade.core.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import Ontologies.*;
import Behaviours.AgentForWbBehaviour;

public class CsAgentForWB extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Codec codec = new SLCodec();
	private Ontology ontology = AgentForWbMessageOntology.getInstance();

	public void setup() {    
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			System.out.println("CsAgentForWB.setup()");
			
			this.addBehaviour(new AgentForWbBehaviour(this));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	    
	}   
}
