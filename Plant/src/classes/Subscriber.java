package classes;

import java.util.Date;
import jade.proto.SubscriptionResponder.Subscription;
import onto.SubscriptionMessage.SubscriptionMessageType;

/**
 * Subscriber information
 */
public class Subscriber 
{	
	private Subscription sub;
	private SubscriptionMessageType type;
	private Date date;
	
	public SubscriptionMessageType getType()
	{
		return this.type;
	}
	
	public Subscription getSub()
	{
		return this.sub;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public Subscriber(Subscription sub, SubscriptionMessageType type, Date date)
	{			
		this.type = type;
		this.sub = sub;
		this.date = date;
	}
}
