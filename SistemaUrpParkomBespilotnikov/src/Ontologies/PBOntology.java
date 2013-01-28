package Ontologies;

import Ontologies.ActionMessage;
import Ontologies.SendAction;
import jade.content.onto.*;

public class PBOntology extends BeanOntology {

	private static final long serialVersionUID = 1L;
	public static final String ONTOLOGY_NAME = "Control-System-Agent-For-Park-Ontology";
	private static Ontology instance = new PBOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }
	
	public PBOntology(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		
		try {
			
			add(ActionMessage.class);
			add(SendAction.class);
			add(PlanMessage.class);
			add(SendPlan.class);
		} 
		
		catch(OntologyException oe) {
			oe.printStackTrace(); 
		}
	}

}