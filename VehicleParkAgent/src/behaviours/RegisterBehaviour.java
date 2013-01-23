package behaviours;

import message.VehicleRequest;
import ontology.ControlOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import agents.ControlSystemAgent;

/**
 * Find service in yellow pages
 */
public class RegisterBehaviour extends TickerBehaviour 
{
	private static final long serialVersionUID = 3384651319762649616L;
	//
	protected DFAgentDescription template;
	private ControlSystemAgent controlsystem;
	//
	public RegisterBehaviour(DFAgentDescription template, ControlSystemAgent a, long period)
	{
		super(a, period);
		this.template = template;
		this.controlsystem = a;
	}

	/**
	 * Finding respondent
	 */
	@Override
	protected void onTick() 
	{
		DFAgentDescription[] result;
		try 
		{
			result = DFService.search(controlsystem, template);
			if (result.length > 0) 
			{
				AID subscriptionAgent = result[0].getName();
				try 
				{
					sendMessage(subscriptionAgent);
				} 
				catch (CodecException | OntologyException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//this.cs_uav.addBehaviour(new ControlBehavior(controlsystem, 1000, subscriptionAgent));
				//
				stop();		// Delete behavior
			}
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		} 
	}
	
	private void sendMessage(final AID subscriptionAgent) throws CodecException, OntologyException 
	{
		ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(subscriptionAgent); 
		request.setLanguage(new SLCodec().getName());
		request.setOntology(ControlOntology.getInstance().getName());	
		Action a = new Action();
		a.setActor(this.myAgent.getAID());
		VehicleRequest vr = new VehicleRequest();
		a.setAction(vr);
		this.controlsystem.getContentManager().fillContent(request, a);	
		this.controlsystem.addBehaviour(new AchieveREInitiator(this.controlsystem, request)
								  {
										private static final long serialVersionUID = -7325604010108355418L;

										protected void handleInform(ACLMessage inform)
										{
											try 
											{
												controlsystem.addBehaviour(new ControlBehavior(controlsystem, 1000, subscriptionAgent));
											}
											catch (Exception e) {
												// TODO: handle exception
											}
										}
				
								  });
	}
}

