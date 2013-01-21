package behaviours;

import ontologies.Electricity;
import ontologies.Message;
import ontologies.State;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

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
			Message sm = new Message();
			sm = subManager.windTurbine().getMessage();
			subManager.handleChange(sm, "STATE");
		}
	}
}
