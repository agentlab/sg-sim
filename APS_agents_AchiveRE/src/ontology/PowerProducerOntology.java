package ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class PowerProducerOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8521959630351360165L;
	public static final String ONTOLOGY_NAME = "PowerProducerOntology";
	private static Ontology instance = new PowerProducerOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public PowerProducerOntology(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		
		try {
			//adding classes into ontology
			add(APSDescriptor.class);
			add(PowerPlan.class);
		} catch (BeanOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
