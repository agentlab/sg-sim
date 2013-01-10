package ontology;


import jade.content.onto.Ontology;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;

public class TPPOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2558231023308074358L;
	public static final String ONTOLOGY_NAME = "TPPOntology";
	private static Ontology instance = new TPPOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public TPPOntology(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		try {
			add(TPPblockDescriptor.class);
			add(OneHourResult.class);
			add(TPPblockControlAction.class);
		} catch (BeanOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
