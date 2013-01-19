package onto;

import jade.content.onto.*;

/**
 * Ontology
 */
public class SubscriptionMessageOntology extends BeanOntology 
{
	public static final String ONTOLOGY_NAME = "SubscriptionMessageOntology";
	private static Ontology instance = new SubscriptionMessageOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }
	private static final long serialVersionUID = 1L;
	
	private SubscriptionMessageOntology(String name) 
	{
		super(name);
		try 
		{
			add(SubscriptionMessage.class);
			add(SendSubscriptionMessage.class);
		} 	
		catch(OntologyException oe) 
		{
			oe.printStackTrace(); 
		}
	}
}
