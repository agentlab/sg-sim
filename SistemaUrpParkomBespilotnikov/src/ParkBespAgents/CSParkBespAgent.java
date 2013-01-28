package ParkBespAgents;

import Behaviours.*;
import jade.core.*;
import jade.content.lang.*;
import jade.content.lang.sl.*;
import jade.content.onto.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import Ontologies.*;

public class CSParkBespAgent extends Agent {

	private static final long serialVersionUID = 1L;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = PBOntology.getInstance();

	public void setup() {    
		
		//описание сервиса агента
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("ControlSystemParkBesp");
		sd.setName("ControlSystemParkBesp");
		dfd.addServices(sd);
		//регистрация сервиса
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
				fe.printStackTrace();
		}
		
		try {
			getContentManager().registerLanguage(codec);
			getContentManager().registerOntology(ontology);

			System.out.println("CSParkBespAgent.setup()");
			
			this.addBehaviour(new PBReceiver(this));
		} 
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	    
	} 
}
