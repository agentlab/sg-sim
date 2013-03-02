package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import subscription.BuildingCSAgentSubscrManager;

/**
 * Tick behavior
 */
public class BuildingCSAgentSendingBehaviour extends TickerBehaviour
{
	private static final long serialVersionUID = 7786903538364903210L;
	protected BuildingCSAgentSubscrManager subManager;
	
	public BuildingCSAgentSendingBehaviour(Agent a, long period, BuildingCSAgentSubscrManager subManager) 
	{
		super(a, period);
		this.subManager = subManager;
	}
	
	@Override
	public void onStart() 
	{
		System.out.println("Tick behavior is activated");
	}
	
	@Override
	protected void onTick() 
	{		
		this.subManager.tick();	
	}
}
