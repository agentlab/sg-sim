package agents;

import java.util.Date;

import onto.ControlAction;
import onto.SendSubscriptionMessage;
import onto.SubscriptionMessage;
import classes.Subscriber;

public class CookerAgent extends ApplianceAgent 
{
	private static final long serialVersionUID = 6270802700359467617L;

	// consts
	private static double chill_factor 			= 1.25;
	public static double max_temperature		= 500.0;
	public static double min_temperature		= 100.0;
	//
	private double temperature  				= 0;	//ambient_temperature;

	public CookerAgent()
	{
		this.name = "CookerAgent";
		this.type = "ConsumerelEctronics";	
	}
	
	public SendSubscriptionMessage getNotifyMessage(Subscriber subscriber)
	{
		
		SubscriptionMessage msg = new SubscriptionMessage();
		msg.setSendDate(new Date()); 											// Sending date	
		msg.setType(subscriber.getType());
		switch (subscriber.getType())
		{
			case Temperature:
				msg.setValue(this.temperature);			
				break;
				
			case PowerConsumption:
				msg.setValue(this.getPowerConsumption());
				break;
				
			default:
				break;
		}
		
		SendSubscriptionMessage smsg = new SendSubscriptionMessage();
		smsg.setMessage(msg);
		
		return smsg;
	}
	
	public void controlAppliance(ControlAction ca)
	{
		switch (ca.getCtype())
		{			
			case SetTemperature:
				setTemperature(ca.getValue());
				break;
		}
	}
	
	private double getPowerConsumption()
	{
		double chillPower;
		//
		chillPower = chill_factor * this.temperature;
		//
		return chillPower;
	}
	
	private void setTemperature(double temperature)
	{
		if (temperature > max_temperature)
		{
			temperature = max_temperature;
		}
		else if (temperature < min_temperature)
		{
			temperature = 0;
		}
			
		this.temperature = temperature;
	}
}
