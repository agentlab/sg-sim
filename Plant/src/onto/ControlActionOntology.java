package onto;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

public class ControlActionOntology extends BeanOntology 
{
	private static final long serialVersionUID = -4664044018063940155L;
	public static final String ONTOLOGY_NAME = "ControlActionOntology";
	private static Ontology instance = new ControlActionOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public ControlActionOntology(String name) 
	{
		super(name);
		try 
		{
			add(TryToControl.class);
			add(SetMachineCount.class);
		} 	
		catch(OntologyException oe) 
		{
			oe.printStackTrace(); 
		}
	}

}
