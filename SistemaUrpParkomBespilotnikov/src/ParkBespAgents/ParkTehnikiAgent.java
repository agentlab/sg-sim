package ParkBespAgents;

import Behaviours.*;
import Ontologies.*;
import jade.content.lang.Codec;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.Ontology;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class ParkTehnikiAgent extends Agent {

	private static final long serialVersionUID = 1L;
	private Codec codec = new SLCodec();
	private Ontology ontology = PBOntology.getInstance();

	public void setup() {   
		
		//описание сервиса агента
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("ParkTehniki");
		sd.setName("ParkTehniki");
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

			System.out.println("ParkTehnikiAgent.setup()");
			MessageTemplate template = MessageTemplate.and(
					MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.MatchPerformative(ACLMessage.REQUEST));
	
			this.addBehaviour(new TestAgentResponderBehaviour(this, template));     
			
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	    
	} 

}
