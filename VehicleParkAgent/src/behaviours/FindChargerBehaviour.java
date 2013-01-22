package behaviours;

import message.ChargerMessage;
import message.ChargerRequest;
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
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;
import agents.ParkAgent;
import agents.ParkAgent.VehicleState;

/**
 * Find service in yellow pages
 */
public class FindChargerBehaviour  extends TickerBehaviour 
{
	private static final long serialVersionUID = 3384651319762649616L;
	//
	protected DFAgentDescription template;
	private ParkAgent vehiclepark;
	//
	private AID subscriptionAgent;
	//
	public FindChargerBehaviour(ParkAgent a, long period)
	{
		super(a, period);
		this.vehiclepark = a;
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Charger");
		sd.setName("Charger");
		this.template = new DFAgentDescription();
		this.template.addServices(sd);	
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
			result = DFService.search(vehiclepark, template);
			if (result.length > 0) 
			{
				this.subscriptionAgent = result[0].getName();
				try 
				{
					sendMessage(this.subscriptionAgent);
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
	
	private void sendMessage(AID subscriptionAgent) throws CodecException, OntologyException 
	{
		ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(subscriptionAgent); 
		request.setLanguage(new SLCodec().getName());
		request.setOntology(ControlOntology.getInstance().getName());	
		Action a = new Action();
		a.setActor(this.myAgent.getAID());
		ChargerRequest cr = new ChargerRequest();
		a.setAction(cr);
		this.vehiclepark.getContentManager().fillContent(request, a);	
		this.vehiclepark.addBehaviour(new AchieveREInitiator(this.vehiclepark, request)
								  {
										private static final long serialVersionUID = -7325604010108355418L;

										protected void handleAgree(ACLMessage agree)
										{
											vehiclepark.work_state = VehicleState.charging;
											RequestCharge();
										}
										
										protected void handleRefuse(ACLMessage refuse)
										{
											vehiclepark.work_state = VehicleState.bad_charger;
										}
				
								  });
	}
	
	private void RequestCharge()
	{
		ChargerMessage cm = new ChargerMessage();
		cm.setConsumption(vehiclepark.getConsumption());
		//
		ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(this.subscriptionAgent);
		request.setLanguage(new SLCodec().getName());
		request.setOntology(ControlOntology.getInstance().getName());	
		try {
			this.vehiclepark.getContentManager().fillContent(request, cm);
		} catch (CodecException | OntologyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		this.vehiclepark.addBehaviour(new AchieveREInitiator(this.vehiclepark, request)
								  {
										private static final long serialVersionUID = -7325604010108355418L;
		
										protected void handleAgree(ACLMessage agree)
										{
											vehiclepark.work_state = VehicleState.work;
											vehiclepark.setPercent(1.0);
										}
										
										protected void handleRefuse(ACLMessage refuse)
										{
											vehiclepark.work_state = VehicleState.bad_charger;
										}
										
								  });
	}
}

