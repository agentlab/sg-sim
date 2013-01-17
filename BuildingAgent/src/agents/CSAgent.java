package agents;

import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class CSAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8420609121514670641L;
	protected void setup() {
		System.out.println("CSAgent "+getAID().getName()+" is ready.");
		
		this.getContentManager().registerLanguage(new SLCodec());					
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		
		addBehaviour(new Receive());
	}
	private class Receive extends CyclicBehaviour {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8806820760958301312L;

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage sumEn=myAgent.receive();
			if (sumEn!=null) {
				try {
					InformMessage a =  (InformMessage)getContentManager().extractContent(sumEn);
					int volume=a.getVolume();
					System.out.println("Summary consumering energy= "+volume);
				} catch (UngroundedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CodecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (OntologyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else {
				block();
			}
		}
		
	}
}
