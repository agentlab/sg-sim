package behaviours;

import onto.ControlActionOntology;
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
import agents.ControlAgent;

/**
 * Find service in yellow pages
 */
public class FindPlant extends TickerBehaviour 
{
	private static final long serialVersionUID = 3384651319762649616L;
	//
	protected DFAgentDescription template;
	private ControlAgent control;
	private AID plant;
	//
	public FindPlant(DFAgentDescription template, ControlAgent a, long period)
	{
		super(a, period);
		this.template = template;
		this.control = a;
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
			result = DFService.search(control, template);
			if (result.length > 0) 
			{
				this.plant = result[0].getName();
				//
				try 
				{
					this.tryToControl();
				} 
				catch (CodecException | OntologyException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				stop();		// Delete behavior
			}
		} 
		catch (FIPAException e) 
		{
			e.printStackTrace();
		} 
	}
	
	private void tryToControl() throws CodecException, OntologyException
	{
		ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(this.plant);
		request.setLanguage(new SLCodec().getName());
		request.setOntology(ControlActionOntology.getInstance().getName());	
		Action a = new Action();
		a.setActor(this.myAgent.getAID());
		a.setAction(new onto.TryToControl());
		this.control.getContentManager().fillContent(request, a);	
		this.control.addBehaviour(new AchieveREInitiator(this.control, request)
								  {
									private static final long serialVersionUID = -7325604010108355418L;

										protected void handleInform(ACLMessage inform)
										{
											// ok
											control.addBehaviour(new ControlBehavior(control, 1000, plant));
										}
										
										protected void handleAgree(ACLMessage inform)
										{
											// false
										}
										protected void handleRefuse(ACLMessage inform)
										{
											// false
										}
								  });
	}
}

