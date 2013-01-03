package ontology;


import jade.content.onto.Ontology;
import jade.content.onto.BeanOntology;
import jade.content.onto.BeanOntologyException;

public class HPSOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7676207239184185387L;
	public static final String ONTOLOGY_NAME = "HPSOntology";
	private static Ontology instance = new HPSOntology(ONTOLOGY_NAME);
	public static Ontology getInstance() { return instance; }

	public HPSOntology(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		try {
			add(HPSblockDescriptor.class);
			add(OneHourResult.class);
			add(HPSblockControlAction.class);
		} catch (BeanOntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

}
