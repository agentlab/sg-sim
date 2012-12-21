package Ontologies;

import jade.content.onto.*;

public class AgentForWbMessageOntology extends BeanOntology {

	public static final String ONTOLOGY_NAME = "AgentForWbMessageOntology";
	private static Ontology instance = new AgentForWbMessageOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }
	
	private static final long serialVersionUID = 1L;
	
	public AgentForWbMessageOntology(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		try {
			
			//add(Message.class);
			add(SendMessage.class);
			//add(AnotherAction.class);
		} 
		
		catch(OntologyException oe) {
			oe.printStackTrace(); 
		}
	}

}
