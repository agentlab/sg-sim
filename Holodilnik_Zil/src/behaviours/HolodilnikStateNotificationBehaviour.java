package behaviours;

import ontologies.Electricity;
import ontologies.SendMessage;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class HolodilnikStateNotificationBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 5144767620080730711L;

	protected StateSubscriptionManager subManager;
	
	public HolodilnikStateNotificationBehaviour(Agent a, StateSubscriptionManager subManager) {
		super(a, 5000);
		
		this.subManager = subManager;
	}

	@Override
	protected void onTick() {
		if(this.subManager != null) {
			SendMessage sm = new SendMessage();
			double temperature = subManager.holodilnik().getCurrentTemperature();
			sm.setMsgTemp(temperature);
			subManager.handleChange(sm, "TEMPERATURE");
			
			SendMessage sm2 = new SendMessage();
			Electricity electricity = new Electricity();
			electricity.setAverPower(subManager.holodilnik().getCurrentPower());
			electricity.setTime(5);
			sm2.setMsgEl(electricity);
			subManager.handleChange(sm2, "ELECTRICITY");
		}
	}
}
