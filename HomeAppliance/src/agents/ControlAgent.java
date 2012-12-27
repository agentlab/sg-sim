package agents;

import onto.ControlActionOntology;
import behaviours.ControlBehavior;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;

public class ControlAgent extends Agent 
{
	private static final long serialVersionUID = 7006985625507843980L;
	//
	private Codec codec = new SLCodec(); 									// Codec
	private Ontology ontology = ControlActionOntology.getInstance(); 		// Ontology
	
	@Override
	protected void setup() 
	{
		System.out.println(this.getAID().getName() + " started.");
		//
		this.getContentManager().registerLanguage(codec); 		// Codec registration 
		this.getContentManager().registerOntology(ontology);	// Ontology registration 
		//
		Object[] args = this.getArguments();
		if (args != null && args.length > 0)
		{
			String controlled_agent = (String)args[0];
			addBehaviour(new ControlBehavior(this, 1000, controlled_agent));
		}	
	}

	@Override
	protected void takeDown() 
	{
		System.out.println("Agent " + this.getLocalName() + " is shutting down.");
	}
}
