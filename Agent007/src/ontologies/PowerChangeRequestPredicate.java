package ontologies;

import jade.content.Predicate;

public class PowerChangeRequestPredicate implements Predicate {
	private static final long serialVersionUID = 1L;
	private PowerChangeRequest powerChangeRequest;
	
	public PowerChangeRequestPredicate() {
		powerChangeRequest = null;
	}
	
	public PowerChangeRequest getPowerChangeRequest() {
		return powerChangeRequest;
	}
	
	public void setPowerChangeRequest(PowerChangeRequest newPowerChangeRequest) {
		powerChangeRequest = newPowerChangeRequest;
	}
}
