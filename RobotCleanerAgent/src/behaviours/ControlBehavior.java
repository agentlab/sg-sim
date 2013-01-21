package behaviours;
import ontology.ControlOntology;
import message.ControlAction;
import message.ResponceAction;
import agents.ControlAgent;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;



public class ControlBehavior extends TickerBehaviour
{
	private static final long serialVersionUID = -968236599222892312L;
	private ControlAgent driver;
	//
	private AID subscriptionAgent;
	private boolean state = false;
	//
	private boolean need_charge = false;
	
	public ControlBehavior(ControlAgent driver, long period, AID subscriptionAgent) 
	{
		super(driver, period);
		this.driver = driver;
		this.subscriptionAgent = subscriptionAgent;
	}

	@Override
	public void onStart() {
		System.out.println("Control behavior is activated");
	}
	
	@Override 
	protected void onTick() {	
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
	
	private ControlAction prepareAction() {
		//
		ControlAction ca = new ControlAction();	
		ca.setNeed_charge(this.need_charge);
		ca.setSpeed(Math.random());
		ca.setXpath(Math.random());
		//ca.setZpath(Math.random());
		ca.setYpath(Math.random());
		//----------------------------
		ca.setArea(Math.random());

		return ca;
	}
	
	private void sendMessage(ControlAction ca) throws CodecException, OntologyException 
	{
		ACLMessage request = new ACLMessage (ACLMessage.REQUEST);
		request.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		request.addReceiver(subscriptionAgent); 
		request.setLanguage(new SLCodec().getName());
		request.setOntology(ControlOntology.getInstance().getName());	
		Action a = new Action();
		a.setActor(this.myAgent.getAID());
		a.setAction(ca);
		this.driver.getContentManager().fillContent(request, a);	
		this.driver.addBehaviour(new AchieveREInitiator(this.driver, request)
								  {
										private static final long serialVersionUID = -7325604010108355418L;

										protected void handleInform(ACLMessage inform)
										{
											try 
											{
												ResponceAction a = (ResponceAction) driver.getContentManager().extractContent(inform);
												
												if (a instanceof ResponceAction)
												{	
													if (state == true)
													{
														if (!a.isState())
														{
															need_charge = true;
														}
														else
														{
															need_charge = false;
														}
													}
													else
													{
														need_charge = false;
													}
													state = a.isState();
													
												}	
											}
											catch (Exception e) {
												// TODO: handle exception
											}
										}
				
								  });
	}

}
