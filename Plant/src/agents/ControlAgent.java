package agents;

import onto.ControlActionOntology;
import behaviours.FindPlant;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

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
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Plants");
		sd.setName("PlantAgent");
		DFAgentDescription template = new DFAgentDescription();
		template.addServices(sd);
		this.addBehaviour(new FindPlant(template, this,1000));
	}

	@Override
	protected void takeDown() 
	{
		System.out.println("Agent " + this.getLocalName() + " is shutting down.");
	}
}
