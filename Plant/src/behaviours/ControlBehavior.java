package behaviours;

import onto.ControlActionOntology;
import onto.SetMachineCount;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import agents.PlantAgent;
import agents.ControlAgent;


public class ControlBehavior extends TickerBehaviour
{
	private static final long serialVersionUID = -968236599222892312L;
	private ControlAgent control;
	//
	private int machine_count = 0;
	//
	private AID plant;
	//

	public ControlBehavior(ControlAgent a, long period, AID plant) 
	{
		super(a, period);
		this.control = a;
		this.plant = plant;
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
	
	private SetMachineCount prepareAction()
	{
		this.machine_count++;
		if (this.machine_count > PlantAgent.max_machine_count)
		{
			this.machine_count = PlantAgent.min_machine_count;
		}
		SetMachineCount ca = new SetMachineCount();	
		ca.setMachine_count(this.machine_count);
		return ca;
	}
	
	private void sendMessage(SetMachineCount ca) throws CodecException, OntologyException
	{
		ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(this.plant);
		request.setLanguage(new SLCodec().getName());
		request.setOntology(ControlActionOntology.getInstance().getName());	
		Action a = new Action();
		a.setActor(this.myAgent.getAID());
		a.setAction(ca);
		this.control.getContentManager().fillContent(request, a);	
		this.control.addBehaviour(new AchieveREInitiator(this.control, request)
								  {
									private static final long serialVersionUID = -7325604010108355418L;

										protected void handleAgree(ACLMessage inform)
										{
											// ok
										}
										protected void handleInform(ACLMessage inform)
										{
											// ok
										}
										protected void handleRefuse(ACLMessage inform)
										{
											// false
										}
								  });
	}

}
