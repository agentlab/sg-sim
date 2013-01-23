package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import ontologies.*;

public class WindTurbineSubscriptionBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private int power;
	private int maxPower;
	private StateSubscriptionManager subManager;

	public WindTurbineSubscriptionBehaviour(Agent a, int power, int maxPower, StateSubscriptionManager subManager) {
		super(a, 5000);
		this.power = power;
		this.maxPower = maxPower;
		this.subManager=subManager;
		this.subManager.setWindTurbine(this);
	}

	@Override
	public void onTick() {
		System.out.println("Power: " + this.power);		
		System.out.println("Max power: " + this.maxPower);
	}

	public State getState() {
		State state = new State();
		state.setPower(power);
		state.setMaxPower(maxPower);
		return state;
	}

	public double getPower() {
		return power;
	}

	public double getMaxPower() {
		return maxPower;
	}
}