package agents;


import jade.domain.DFService;
import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class WholeSaleBroker extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -741223470022374171L;
	public int Evalue;
	public AID bestSeller;
	public int bestPrice;
	public String name="WholesaleBrokerAgent";
	public String type="WBA";
	public int price;

	protected void setup() {
		// Printout a welcome message
		System.out.println("Wholesale Broker agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());					//registracia yazika
		this.getContentManager().registerOntology(RequestOntology.getInstance());	//registracia ontologii
		this.createResponder();
		this.yellowPagesRegister();
	}
	private void yellowPagesRegister() {
		// TODO Auto-generated method stub
		// Service registering on yellow page service
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.type); 
		sd.setName(this.name);
		//
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(this.getAID());
		dfd.addServices(sd);
		try 
		{
			DFService.register(this, dfd);
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}		
	}

	private void createResponder()	
	{
		MessageTemplate mtr = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new AchieveREResponder(this, mtr) {						  

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			protected ACLMessage prepareResultNotification(ACLMessage request, ACLMessage response) {				  
				try {
					InformMessage a =  (InformMessage)getContentManager().extractContent(request);
					System.out.println("Report recieved "+a.getHourReport());
				}									

				catch (UngroundedException e) {					
					e.printStackTrace();
				} 
				catch (CodecException e) {									
					e.printStackTrace();
				} 
				catch (OntologyException e) {									
					e.printStackTrace();
				} 								    
				ACLMessage informDone = request.createReply();
				informDone.setPerformative(ACLMessage.INFORM);
				informDone.setContent("Report received");								  
				return informDone;					
			}							
		});
	}

}
