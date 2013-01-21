package agents;

import message.ControlAction;
import message.ResponceAction;
import message.VehicleRelease;
import message.VehicleRequest;
import ontology.ControlOntology;
import jade.content.Concept;
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

public abstract class ElectroVehicle extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6884488784171517606L;
	//
	protected String name;
	protected String type;
	//
	private boolean is_registered = false;
	//
	public enum VehicleState
	{
		work,
		charging,
		finding_charger,
		bad_charger
	}
	public VehicleState work_state = VehicleState.work;
	//
	private double consumption = 0.0;
	private double percent = 1.0;
	
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
	
	public abstract void controlVehicle(ControlAction a);
	public abstract ResponceAction getResponce();
	
	private void createResponder()
	{   
		MessageTemplate mtr = AchieveREResponder.createMessageTemplate(FIPANames.InteractionProtocol.FIPA_REQUEST);
		this.addBehaviour(new AchieveREResponder(this, mtr)
						  {
							private static final long serialVersionUID = 99691474816159152L;

								protected ACLMessage prepareResponse(ACLMessage request)
								{
									// 
									try 
									{
									   // прием сообщения
										Action a = (Action) getContentManager().extractContent(request); // извлекаем контент
										Concept ca = a.getAction();

										if (ca instanceof VehicleRequest)
										{		
											yp_deregister();
										}
										else if (ca instanceof ControlAction)
										{
											controlVehicle((ControlAction)ca);
										}
										else if (ca instanceof VehicleRelease)
										{
											yp_register();
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
									} 
								    //
									
									ACLMessage informDone = request.createReply();
									informDone.setPerformative(ACLMessage.INFORM);
								    informDone.setLanguage(new SLCodec().getName());
								    informDone.setOntology(ControlOntology.getInstance().getName());
								    try {
										getContentManager().fillContent(informDone, getResponce());
									} catch (CodecException | OntologyException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
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
			this.is_registered = true;
		} 
		catch (FIPAException fe) 
		{
			fe.printStackTrace();
		}	
	}
	
	private void yp_deregister()
	{
		if (this.is_registered)
		{
			try 
			{
				DFService.deregister(this);
				this.is_registered = false;
			} 
			catch (FIPAException fe) 
			{
				fe.printStackTrace();
			}
		}
	}

	/**
	 * @return the consumption
	 */
	public double getConsumption() {
		return consumption;
	}

	/**
	 * @param consumption the consumption to set
	 */
	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

	/**
	 * @return the percent
	 */
	public double getPercent() {
		return percent;
	}

	/**
	 * @param percent the percent to set
	 */
	public void setPercent(double percent) {
		this.percent = percent;
	}
}
