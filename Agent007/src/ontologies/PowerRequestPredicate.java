package ontologies;

import jade.content.Predicate;

public class PowerRequestPredicate implements Predicate {
	private static final long serialVersionUID = 1L;
	private PowerRequest powerRequest;
	
	public PowerRequestPredicate() {
		powerRequest = null;
	}
	
	public PowerRequest getPowerRequest() {
		return powerRequest;
	}
	
	public void setPowerRequest(PowerRequest newPowerRequest) {
		powerRequest = newPowerRequest;
	}
}
