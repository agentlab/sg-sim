package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import subscription.SubscrManager;

/**
 * Tick behavior
 */
public class SendingBehaviour extends TickerBehaviour
{
	private static final long serialVersionUID = 7786903538364903210L;
	protected SubscrManager subManager;
	
	public SendingBehaviour(Agent a, long period, SubscrManager subManager) 
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
