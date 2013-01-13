package ontologies;

import internal_classes.AssignPowerRequest;
import internal_classes.BeginPowerTransitionRequest;
import internal_classes.Electricity;
import internal_classes.SendMessage;
import internal_classes.State;
import internal_classes.TimeDelay;
import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

public class GydroelectricpowerBlockAgentOntology extends BeanOntology {
	
	public static final String ONTOLOGY_NAME = "GydroelectricpowerBlockAgentOntology";
	private static Ontology instance = new GydroelectricpowerBlockAgentOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public GydroelectricpowerBlockAgentOntology(String name) {
		super(name);
		
		try {
			add(Electricity.class);
			add(TimeDelay.class);
			add(State.class);
			add(SendMessage.class);
			add(AssignPowerRequest.class);
			add(BeginPowerTransitionRequest.class);
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
