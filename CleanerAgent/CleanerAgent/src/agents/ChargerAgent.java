package agents;

import message.ChargerMessage;
import message.ChargerRequest;
import ontology.ControlOntology;
import jade.content.Concept;
import jade.content.ContentElement;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.UngroundedException;
import jade.content.onto.basic.Action;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.AchieveREResponder;

public class ChargerAgent extends Agent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5744471185466290089L;
	//
	protected String name = "Charger";
	protected String type = "Charger";
	
	@Override
	protected void setup(){
		System.out.println(this.getAID().getName() + " started.");
		//
		this.registerOntology();
		//
		this.yp_register();
		//
		this.createResponder();
	}

	@Override
	protected void takeDown(){
		System.out.println(this.getAID().getName() + " ended.");
	}
	
	private void registerOntology()
	{
		this.getContentManager().registerLanguage(new SLCodec());
		this.getContentManager().registerOntology(ControlOntology.getInstance());
	}
	
	private void createResponder()
	{
		MessageTemplate mtr = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new AchieveREResponder(this, mtr)
						  {
							private static final long serialVersionUID = 99691474816159152L;

							protected ACLMessage prepareResponse(ACLMessage request)
							   {
								   try 
									{
										ContentElement a = getContentManager().extractContent(request);
										
										if (a instanceof Action)
										{
											Concept ca = ((Action) a).getAction();
											
											if (ca instanceof ChargerRequest)
											{
												ACLMessage informDone = request.createReply();
											    informDone.setPerformative(ACLMessage.AGREE);
											    informDone.setContent("ok");
											    return informDone;
											}
										}
										else if (a instanceof ChargerMessage)
										{	
											double delay = ((ChargerMessage) a).getConsumption();	
											Thread.sleep((long) (delay * 1000));
											ACLMessage informDone = request.createReply();
										    informDone.setPerformative(ACLMessage.AGREE);
										    informDone.setContent("ok");
										    return informDone;
										}
																			
									} 
									catch (UngroundedException e) 
									{
										e.printStackTrace();
									} 
									catch (CodecException e) 
									{
										e.printStackTrace();
									} 
									catch (OntologyException e) 
									{
										e.printStackTrace();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} 
								    //
								    ACLMessage informDone = request.createReply();
								    informDone.setPerformative(ACLMessage.INFORM);
								    informDone.setContent("bad message");
								    return informDone;
							   }
						  });
	}
	
	private void yp_register()
	{
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
}
