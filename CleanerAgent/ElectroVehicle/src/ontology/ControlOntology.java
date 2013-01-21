package ontology;

import message.ChargerMessage;
import message.ChargerRequest;
import message.ControlAction;
import message.ResponceAction;
import message.VehicleRelease;
import message.VehicleRequest;
import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;

public class ControlOntology extends BeanOntology 
{
	private static final long serialVersionUID = -4664044018063940155L;
	public static final String ONTOLOGY_NAME = "ControlActionOntology";
	private static Ontology instance = new ControlOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public ControlOntology(String name) 
	{
		super(name);
		try 
		{
			add(ChargerRequest.class);
			add(VehicleRelease.class);
			add(VehicleRequest.class);
			add(ControlAction.class);
			add(ResponceAction.class);
			add(ChargerMessage.class);
		} 	
		catch(OntologyException oe) 
		{
			oe.printStackTrace(); 
		}
	}
}