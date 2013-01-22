package Ontology;

import Ontology.RequestOntology;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;
import jade.content.onto.Ontology;

public class RequestOntology extends BeanOntology{


	private static final long serialVersionUID = -8521605939127719042L;
	public static final String ONTOLOGY_NAME = "RequestOntology";
	private static Ontology instance = new RequestOntology(ONTOLOGY_NAME);
	public static Ontology getInstance;
	
	public static Ontology getInstance() { return instance; }
	
	public RequestOntology(String name) {
		super(name);
		
		try {
			this.add(InformMessage.class);
		} catch (BeanOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated constructor stub
	}


}
