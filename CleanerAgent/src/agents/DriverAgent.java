package agents;

import ontology.ControlOntology;
import behaviours.RegisterBehaviour;
import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class DriverAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7018519309996025960L;

	@Override
	protected void setup(){
		System.out.println(this.getAID().getName() + " started.");
		//
		this.registerOntology();
		//
		Object[] args = this.getArguments();
		if (args != null && args.length > 0)
		{
			String controlled_agent = (String)args[0];
			
			ServiceDescription sd = new ServiceDescription();
			sd.setType("ElectroVehicle");
			sd.setName(controlled_agent);
			DFAgentDescription template = new DFAgentDescription();
			template.addServices(sd);	
			addBehaviour(new RegisterBehaviour(template, this, 1000));
		}	
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
}
