package behaviours;

import onto.ControlAction.ControlType;
import onto.ControlActionOntology;
import onto.ControlAction;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import agents.ConditionerAgent;
import agents.ControlAgent;
import agents.CookerAgent;


public class ControlBehavior extends TickerBehaviour
{
	private static final long serialVersionUID = -968236599222892312L;
	private ControlAgent control;
	//
	private static double step = 0.1;
	private double pos = 0.0;
	private String controlled_agent;
	//
	private ControlAction.ControlType message_type;
	//

	public ControlBehavior(ControlAgent a, long period, String controlled_agent) 
	{
		super(a, period);
		this.control = a;
		this.controlled_agent = controlled_agent;
	}
	
	@Override
	public void onStart() 
	{
		System.out.println("Control behavior is activated");
	}
	
	@Override
	protected void onTick() 
	{	
		try 
		{	
			this.sendMessage(this.prepareAction());
		} 
		catch (CodecException e) 
		{
			e.printStackTrace();
		} 
		catch (OntologyException e) 
		{
			e.printStackTrace();
		}
	}
	
	private ControlAction prepareAction()
	{
		this.pos += step;
		double temperature = 0;
		double flow = 0;
		double value = 0;
		//
		switch (this.controlled_agent)
		{
		case "conditioner":
			flow = (ConditionerAgent.max_flow / 2.0) * Math.sin(0.5 * this.pos) + (ConditionerAgent.max_flow / 2.0);
			temperature = (ConditionerAgent.max_temperature / 2.0) * Math.cos(0.5 * this.pos) + (ConditionerAgent.max_temperature / 2.0);
			//
			if (this.message_type == ControlType.SetTemperature)
			{
				this.message_type = ControlType.SetFlow;
				value = flow;
			}
			else
			{
				this.message_type = ControlType.SetTemperature;
				value = temperature;
			}
			break;
			
		case "cooker":
			temperature = (CookerAgent.max_temperature / 2.0) * Math.cos(this.pos) + (CookerAgent.max_temperature / 2.0);
			this.message_type = ControlType.SetTemperature;
			value = temperature;
			break;
		}
		//
		ControlAction ca = new ControlAction();	
		ca.setValue(value);
		ca.setCtype(this.message_type);
		return ca;
	}
	
	private void sendMessage(ControlAction ca) throws CodecException, OntologyException
	{
		ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(new AID(this.controlled_agent, AID.ISLOCALNAME));
		request.setLanguage(new SLCodec().getName());
		request.setOntology(ControlActionOntology.getInstance().getName());	
		Action a = new Action();
		a.setActor(this.myAgent.getAID());
		a.setAction(ca);
		this.control.getContentManager().fillContent(request, a);	
		this.control.addBehaviour(new AchieveREInitiator(this.control, request)
								  {
									private static final long serialVersionUID = -7325604010108355418L;

										protected void handleInform(ACLMessage inform)
										{
											// inform done
										}
								  });
	}

}
