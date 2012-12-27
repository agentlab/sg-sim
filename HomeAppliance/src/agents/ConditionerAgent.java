package agents;

import java.util.Date;
import onto.ControlAction;
import onto.SendSubscriptionMessage;
import onto.SubscriptionMessage;
import classes.Subscriber;

public class ConditionerAgent extends ApplianceAgent
{
	private static final long serialVersionUID = 453392452080656067L;
	// consts
	private static double chill_factor 			= 1.25;
	private static double flow_factor 			= 1.00;
	private static double ambient_temperature 	= 25.0;
	private static double efficiency_ratio		= 0.45;
	public static double max_temperature		= 35.0;
	public static double min_temperature		= 0.0;
	public static double max_flow				= 10;
	public static double min_flow				= 0;
	//
	private double temperature  				= 15;	//ambient_temperature;
	private double flow							= 0;

	public ConditionerAgent()
	{
		this.name = "ConditionerAgent";
		this.type = "ConsumerelEctronics";	
	}
	
	public SendSubscriptionMessage getNotifyMessage(Subscriber subscriber)
	{
		SubscriptionMessage msg = new SubscriptionMessage();
		msg.setSendDate(new Date()); 											// Sending date	
		msg.setType(subscriber.getType());
		switch (subscriber.getType())
		{
			case CoolingCapacity:
				msg.setValue(this.getCoolingCapacity());			
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
			case SetFlow:
				setFlow(ca.getValue());
				break;
				
			case SetTemperature:
				setTemperature(ca.getValue());
				break;
		}
	}
	
	private double getPowerConsumption()
	{
		double chillPower;
		double flowPower;
		//
		chillPower = chill_factor * Math.abs(ambient_temperature - this.temperature);
		flowPower = flow_factor * Math.abs(this.flow);
		//
		return chillPower + flowPower;
	}
	
	private double getCoolingCapacity()
	{
		return efficiency_ratio * this.flow * Math.abs(ambient_temperature - this.temperature);
	}
	
	private void setTemperature(double temperature)
	{
		if (temperature > max_temperature)
		{
			temperature = max_temperature;
		}
		else if (temperature < min_temperature)
		{
			temperature = min_temperature;
		}
			
		this.temperature = temperature;
	}
	
	private void setFlow(double flow)
	{
		if (flow > max_flow)
		{
			flow = max_flow;
		}
		else if (flow < min_flow)
		{
			flow = min_flow;
		}
			
		this.flow = flow;
	}
}
