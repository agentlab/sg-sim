package onto;

import jade.content.Concept;
import java.util.Date;

/**
 * Message context
 */
public class SubscriptionMessage implements Concept
{
	public static enum SubscriptionMessageType 
	{
		PowerConsumption, 
		CoolingCapacity,
		Temperature
	};
	private static final long serialVersionUID = -4331343106987983326L;	
	//
	private Date 					sendDate;
	private double					value;
	private SubscriptionMessageType type;
	
	public void setValue(double value)
	{
		this.value = value;
	}
	
	public double getValue()
	{
		return this.value;
	}
	
	public void setSendDate(Date sendDate) 
	{
		this.sendDate = sendDate;
	}
	
	public Date getSendDate() 
	{
		return this.sendDate;
	}
	
	public void setType(SubscriptionMessageType type)
	{
		this.type = type;
	}
	
	public SubscriptionMessageType getType()
	{
		return this.type;
	}
}
