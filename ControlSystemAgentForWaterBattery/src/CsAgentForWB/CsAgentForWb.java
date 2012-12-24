package CsAgentForWB;


import Behaviours.AgentForWbBehaviour;
import jade.core.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import Ontologies.*;

public class CsAgentForWb extends Agent {

	private static final long serialVersionUID = 1L;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = AgentForWbOntology.getInstance();

	public void setup() {    
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			System.out.println("CsAgentForWb.setup()");
			
			this.addBehaviour(new AgentForWbBehaviour(this));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	    
	} 
}
