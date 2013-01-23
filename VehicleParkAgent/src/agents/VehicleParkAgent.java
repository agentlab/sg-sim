package agents;

import behaviours.FindChargerBehaviour;
import message.ControlAction;
import message.ResponceAction;

public class VehicleParkAgent extends ParkAgent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5936281111400616639L;
	//
	private double power = 0;
	//
	
	//
	private boolean can_work = true;
	
	public VehicleParkAgent()
	{
		this.name = "VehicleParkAgent";
		this.type = "ParkAgent";
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
					
					double s = a.getNeedPower();

					double p = this.getPercent() - 0.1 * s;
					
					if (p > 0)
					{
						this.can_work = true;
						this.setPercent(p);
						this.power = a.getNeedPower();
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
