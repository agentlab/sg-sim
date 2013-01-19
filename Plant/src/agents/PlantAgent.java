package agents;

import java.util.Date;
import onto.SetMachineCount;
import onto.SendSubscriptionMessage;
import onto.SubscriptionMessage;
import classes.Subscriber;

public class PlantAgent extends BasePlantAgent
{
	private static final long serialVersionUID = 453392452080656067L;
	//
	public static int min_machine_count = 0;
	public static int max_machine_count = 10;
	// consts
	private static double machine_consumption	= 1.25;
	private int machine_count = min_machine_count;

	public PlantAgent()
	{
		this.name = "PlantAgent";
		this.type = "Plants";	
	}
	
	public SendSubscriptionMessage getNotifyMessage(Subscriber subscriber)
	{
		SubscriptionMessage msg = new SubscriptionMessage();
		msg.setSendDate(new Date()); 											// Sending date	
		msg.setType(subscriber.getType());
		switch (subscriber.getType())
		{		
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
	
	public void controlAppliance(SetMachineCount ca)
	{
		this.machine_count = ca.getMachine_count();
	}
	
	private double getPowerConsumption()
	{
		return this.machine_count * PlantAgent.machine_consumption;
	}
}
