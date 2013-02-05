package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import subscription.CustomerAgentSubsrManager;

/**
 * Tick behavior
 */
public class CustomerAgentSendingBehaviour extends TickerBehaviour
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7472517142816339489L;
	protected CustomerAgentSubsrManager subManager;
	
	public CustomerAgentSendingBehaviour(Agent a, long period, CustomerAgentSubsrManager subManager) 
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
