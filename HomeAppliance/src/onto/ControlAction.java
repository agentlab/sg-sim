package onto;

import jade.content.AgentAction;

public class ControlAction implements AgentAction
{
	private static final long serialVersionUID = 5281179776722803352L;
	//
	public static enum ControlType
	{
		SetTemperature,
		SetFlow
	}
	//
	private double value;
	private ControlType ctype;

	public double getValue() 
	{
		return value;
	}

	public void setValue(double value) 
	{
		this.value = value;
	}

	public ControlType getCtype() 
	{
		return ctype;
	}

	public void setCtype(ControlType ctype) 
	{
		this.ctype = ctype;
	}
}
