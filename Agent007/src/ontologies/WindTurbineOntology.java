package ontologies;

import jade.content.onto.BasicOntology;
import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

public class WindTurbineOntology extends BeanOntology {
	private static final long serialVersionUID = 1L;

	public static final String ONTOLOGY_NAME = "Wind_Turbine_Ontology";

	private static Ontology instance = new WindTurbineOntology(ONTOLOGY_NAME);
	
	public static Ontology getInstance() {
		return instance;
	}

	public WindTurbineOntology(String name) {
		super(name, BasicOntology.getInstance());

		try {
			add(PowerRequest.class);
			add(PowerRequestPredicate.class);
			add(PowerChangeRequest.class);
			add(PowerChangeRequestPredicate.class);
		} catch(OntologyException oe) {
			oe.printStackTrace();
		}
	}
}