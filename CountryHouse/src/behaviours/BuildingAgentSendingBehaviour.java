package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import subscription.BuildingAgentSubscrManager;

/**
 * Tick behavior
 */
public class BuildingAgentSendingBehaviour extends TickerBehaviour
{
	private static final long serialVersionUID = 7786903538364903210L;
	protected BuildingAgentSubscrManager subManager;
	
	public BuildingAgentSendingBehaviour(Agent a, long period, BuildingAgentSubscrManager subManager) 
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
