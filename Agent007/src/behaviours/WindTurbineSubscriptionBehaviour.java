package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import ontologies.*;
import sg_sim.WindTurbineAgent;

public class WindTurbineSubscriptionBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private StateSubscriptionManager subManager;
	WindTurbineAgent windTurbineAgent;

	public WindTurbineSubscriptionBehaviour(WindTurbineAgent a, StateSubscriptionManager subManager) {
		super(a, 5000);
		windTurbineAgent = a;
		this.subManager=subManager;
		this.subManager.setWindTurbine(this);
	}

	@Override
	public void onTick() {
		System.out.println("Power: " + windTurbineAgent.getPower());		
		System.out.println("Max power: " + windTurbineAgent.getMaxPower());
	}

	public State getState() {
		State state = new State();
		state.setPower(windTurbineAgent.getPower());
		state.setMaxPower(windTurbineAgent.getMaxPower());
		return state;
	}

	public double getPower() {
		return windTurbineAgent.getPower();
	}

	public double getMaxPower() {
		return windTurbineAgent.getMaxPower();
	}
}