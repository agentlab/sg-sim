package ontologies;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

public class WaterBatteryOntology extends BeanOntology {
	
	public static final String ONTOLOGY_NAME = "Water_battery_Ontology";
	private static Ontology instance = new WaterBatteryOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public WaterBatteryOntology(String name) {
		super(name);
		
		try {
			add(Electricity.class);
			add(TimeDelay.class);
			add(State.class);
			//add(Message.class);
			add(SendMessage.class);
			add(AssignPowerRequest.class);
			add(BeginWaterTransitionRequest.class);
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
