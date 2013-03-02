package onto;

import jade.content.*;	//AgentAction 

public class SendSubscriptionMessage implements Predicate
{
	private static final long serialVersionUID = 6876274843885978495L;
	
	private SubscriptionMessage message;
	
	public SubscriptionMessage getMessage() 
	{
		return this.message;
	}
	
	public void setMessage(SubscriptionMessage msg) 
	{
		this.message = msg;
	}
	
}