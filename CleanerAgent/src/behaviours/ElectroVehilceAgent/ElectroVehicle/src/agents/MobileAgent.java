package agents;

import behaviours.FindChargerBehaviour;
import message.ControlAction;
import message.ResponceAction;

public class MobileAgent extends ElectroVehicle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5936281111400616639L;
	//
	private double x = 0;
	private double y = 0;
	private double speed;
	//
	
	//
	private boolean can_work = true;
	
	public MobileAgent()
	{
		this.name = "MobileAgent";
		this.type = "ElectroVehicle";
	}
	

	@Override
	public void controlVehicle(ControlAction a) 
	{
		switch (this.work_state)
		{
			case work:
				if (a.isNeed_charge())
				{
					this.work_state = VehicleState.finding_charger;
					this.setConsumption((100-100*this.getPercent()) * 0.1);
					this.addBehaviour(new FindChargerBehaviour(this, 1000));	
				} 
				else
				{
					double s = Math.sqrt((this.x - a.getXpath()) * (this.x - a.getXpath()) + 
										 (this.y - a.getYpath()) * (this.y - a.getYpath()));
					//
					this.speed = a.getSpeed();
					//
					double p = this.getPercent() - 0.1 * s * this.speed;
					
					if (p > 0)
					{
						this.can_work = true;
						this.setPercent(p);
						this.x = a.getXpath();
						this.y = a.getYpath();
					}
					else
					{
						this.can_work = false;
					}
				}
				break;
				
			case charging:
				this.can_work = false;
				break;
				
			case finding_charger:
				//TODO
				break;
				
			case bad_charger:
				if (a.isNeed_charge())
				{
					this.work_state = VehicleState.finding_charger;
					this.setConsumption((100-100*this.getPercent()) * 0.1);
					this.addBehaviour(new FindChargerBehaviour(this, 1000));
				} 
				break;
				
			default:
				break;
		}
	}

	@Override
	public ResponceAction getResponce() {
		ResponceAction ra = new ResponceAction();
		//
		ra.setPercent(this.getPercent());
		ra.setState(this.can_work);
		return ra;
	}
}
