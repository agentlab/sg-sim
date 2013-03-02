package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import subscription.BOAgentSubscrManager;

/**
 * Tick behavior
 */
public class BOAgentSendingBehaviour extends TickerBehaviour
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7472517142816339489L;
	protected BOAgentSubscrManager subManager;
	
	public BOAgentSendingBehaviour(Agent a, long period, BOAgentSubscrManager subManager) 
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
