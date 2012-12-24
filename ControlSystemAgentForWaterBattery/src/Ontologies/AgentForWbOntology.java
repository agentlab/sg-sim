package Ontologies;

import Ontologies.Message;
import Ontologies.SendMessage;
import jade.content.onto.*;

public class AgentForWbOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String ONTOLOGY_NAME = "Control-System-Agent-For-WB-Ontology";
	private static Ontology instance = new AgentForWbOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }
	
	public AgentForWbOntology(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		
		try {
			
			add(Message.class);
			add(SendMessage.class);
			//add(AnotherAction.class);
		} 
		
		catch(OntologyException oe) {
			oe.printStackTrace(); 
		}
	}

}
