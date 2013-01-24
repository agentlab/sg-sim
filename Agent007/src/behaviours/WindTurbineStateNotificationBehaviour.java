package behaviours;

import ontologies.Electricity;
import ontologies.SendMessage;
import ontologies.State;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class WindTurbineStateNotificationBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 5144767620080730711L;

	protected StateSubscriptionManager subManager;

	public WindTurbineStateNotificationBehaviour(Agent a, StateSubscriptionManager subManager) {
		super(a, 5000);
		this.subManager = subManager;
	}

	@Override
	protected void onTick() {
		if(this.subManager != null) {
			SendMessage sm = new SendMessage();
			State state = subManager.windTurbine().getState();
			sm.setMsg(state);
			subManager.handleChange(sm, "STATE");

			SendMessage sm2 = new SendMessage();
			Electricity electricity = new Electricity();
			electricity.setAverPower(subManager.windTurbine().getPower());
			electricity.setTime(3600);
			sm2.setMsg(electricity);
			subManager.handleChange(sm, "ELECTRICITY");
		}
	}
}