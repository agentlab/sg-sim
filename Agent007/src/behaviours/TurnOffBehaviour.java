package behaviours;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.WakerBehaviour;

public class TurnOffBehaviour extends WakerBehaviour {
    private static final long serialVersionUID = 1L;
	private Behaviour behaviour; 
	
	public TurnOffBehaviour(Agent a, long timeout, Behaviour behaviour) {
		super(a, timeout);
		this.behaviour = behaviour;
	}
	
	public void onWake() {
		myAgent.addBehaviour(behaviour);
	}
}
