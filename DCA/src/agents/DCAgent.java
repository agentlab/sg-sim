package agents;


import Ontology.InformMessage;
import Ontology.RequestOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;


public class DCAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int volume;
	public int hourSum;
	public int daySum;
	public int msgDayCnt;
	public int msgHourCnt;
	public String name = "DistributionCompanyAgent";		// Agent's name on yellow page service
	public String type = "DCA";		// Agent's type on yellow page service
	public DFAgentDescription[] results;
	protected void setup(){
		System.out.println("Destribution Agent "+getAID().getName()+" is ready.");
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(RequestOntology.getInstance());
		this.yellowPagesRegister();
		System.out.println("Agent "+getLocalName()+" searching for services of type \"Retail Broker\"");
		try {
			// Build the description used as template for the search
			DFAgentDescription template = new DFAgentDescription();
			ServiceDescription templateSd = new ServiceDescription();
			templateSd.setType("RTA");
			template.addServices(templateSd);
			SearchConstraints sc = new SearchConstraints();
			results = DFService.search(this, template, sc);
			if (results.length > 0) {
				System.out.println("Agent "+getLocalName()+" found the Retail Broker:");
				for (int i = 0; i < results.length; ++i) {
					DFAgentDescription dfd = results[i];
					AID provider = dfd.getName();
					Iterator it = dfd.getAllServices();
					while (it.hasNext()) {
						ServiceDescription sd = (ServiceDescription) it.next();
						if (sd.getType().equals("RTA")) {
							System.out.println("- Service \""+sd.getName()+"\" provided by agent "+provider.getName());
						}
					}
				}
			}	
			else {
				System.out.println("Agent "+getLocalName()+" did not find any RT Broker");
			}
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		ACLMessage subscribe=new ACLMessage(ACLMessage.SUBSCRIBE);
		subscribe.setLanguage(new SLCodec().getName());
		subscribe.setOntology(RequestOntology.getInstance().getName());
		subscribe.setProtocol(FIPANames.InteractionProtocol.FIPA_SUBSCRIBE);
		subscribe.setContent("Value");
		subscribe.addReceiver(results[0].getName());
		this.addBehaviour(new DCAgentSubscrInit(this,subscribe));
	}

	private void yellowPagesRegister() {
		// TODO Auto-generated method stub
		// Service registering on yellow page service
		ServiceDescription sd = new ServiceDescription();
		sd.setType(this.type); 
		sd.setName(this.name);
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

	private class DCAgentSubscrInit extends SubscriptionInitiator{

		/**
		 * 
		 */
		private static final long serialVersionUID = -6893314277357566360L;
		public DCAgentSubscrInit(Agent a, ACLMessage msg) {
			super(a, msg);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void handleRefuse(ACLMessage refuse) {
			/**
			 * 
			 */
			System.out.println("Warning. Retail Broker "+refuse.getSender().getName()+" refused subscription.");
		}
		@Override
		protected void handleInform(ACLMessage msg) {
			/**
			 * handle recieved notification message
			 */

			// TODO Auto-generated method stub

			if (msg!=null){
				try {
					InformMessage a = (InformMessage)getContentManager().extractContent(msg);
					volume= a.getVolume();
					hourSum=hourSum+volume;
					daySum=daySum+volume;
					msgHourCnt++;
					msgDayCnt++;

					//Sending hour report to Wholesale Broker
					if (msgHourCnt==12) {
						System.out.println("Hour repot "+hourSum);
						ACLMessage sndHourReport = new ACLMessage (ACLMessage.REQUEST);
						sndHourReport.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
						sndHourReport.addReceiver(new AID("WholeSaleBroker",AID.ISLOCALNAME));
						sndHourReport.setLanguage(new SLCodec().getName());	
						sndHourReport.setOntology(RequestOntology.getInstance().getName());
						InformMessage imessage = new InformMessage();
						imessage.setHourReport(hourSum);
						try {
							(this.myAgent).getContentManager().fillContent(sndHourReport, imessage);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						(this.myAgent).addBehaviour(new AchieveREInitiator(this.myAgent, sndHourReport)
						{	
							private static final long serialVersionUID = 1L;

							protected void handleInform(ACLMessage inform)
							{	
							}
						});
						hourSum=0;
						msgHourCnt=0;
					}
					if (msgDayCnt==33) {
						System.out.println("Day repot "+daySum);
						ACLMessage sndDayReport = new ACLMessage (ACLMessage.REQUEST);
						sndDayReport.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
						sndDayReport.addReceiver(new AID("WholeSaleBroker",AID.ISLOCALNAME));
						sndDayReport.setLanguage(new SLCodec().getName());	
						sndDayReport.setConversationId("Day Report");
						sndDayReport.setOntology(RequestOntology.getInstance().getName());
						InformMessage imessage = new InformMessage();
						imessage.setDayReport(daySum);
						try {
							(this.myAgent).getContentManager().fillContent(sndDayReport, imessage);
						} catch (CodecException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (OntologyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				
						(this.myAgent).addBehaviour(new AchieveREInitiator(this.myAgent, sndDayReport)
						{	
							private static final long serialVersionUID = 1L;

							protected void handleInform(ACLMessage inform)
							{	

							}
						});
						daySum=0;
						msgDayCnt=0;
					}
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
		}
	}
}
