package ontologies;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

public class HolodilnikOntology extends BeanOntology {
	
	public static final String ONTOLOGY_NAME = "HolodilnikOntology";
	private static Ontology instance = new HolodilnikOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public HolodilnikOntology(String name) {
		super(name);
		
		try {
			add(Electricity.class);
			//add(Message.class);
			add(SendMessage.class);
			add(AssignTemperatureRequest.class);
			add(BeginTemperatureTransitionRequest.class);
		} 
		
		catch(OntologyException oe) {
			oe.printStackTrace(); //!
		}
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
