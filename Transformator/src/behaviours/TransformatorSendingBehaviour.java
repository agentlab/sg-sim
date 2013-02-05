package behaviours;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import subscription.TransformatorSubscrManager;

/**
 * Tick behavior
 */
public class TransformatorSendingBehaviour extends TickerBehaviour
{
	private static final long serialVersionUID = 7786903538364903210L;
	protected TransformatorSubscrManager subManager;
	
	public TransformatorSendingBehaviour(Agent a, long period, TransformatorSubscrManager subManager) 
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
