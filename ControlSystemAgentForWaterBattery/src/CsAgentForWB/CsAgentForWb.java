package CsAgentForWB;


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

public class CsAgentForWb extends Agent {

	private static final long serialVersionUID = 1L;
	
	private Codec codec = new SLCodec();
	private Ontology ontology = AgentForWbOntology.getInstance();

	public void setup() {    
		
		//описание сервиса агента
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Control System");
		sd.setName("CS-Agent-For-Water-Battery");
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

			System.out.println("CsAgentForWb.setup()");
			
			this.addBehaviour(new AgentForWbReciever(this));
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	    
	} 
}
