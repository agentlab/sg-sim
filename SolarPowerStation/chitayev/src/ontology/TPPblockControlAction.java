package ontology;

import jade.content.AgentAction;

public class TPPblockControlAction implements AgentAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5382836181699373324L;
	public static enum ControlType{
		Stop,
		Start,
		SetPower,
		NoAction
	}
	
	//мощность+тип дейтвия
	private double power;
	private ControlType actionType;
	
	//остановить блок
	public TPPblockControlAction() {
		this.actionType=ControlType.Stop;
		this.power=0;
	}
	
	//производство энергии
	public TPPblockControlAction(double power, ControlType actionType) {
		this.power = power;
		this.actionType = actionType;
	}

	//текущее состояние
	public ControlType getActionType() {
		return actionType;
	}

	//установить состояние
	public void setActionType(ControlType actionType) {
		this.actionType = actionType;
	}

	//текущая мощность
	public double getPower() {
		return power;
	}

	//задать мощность
	public void setPower(double power) {
		this.power = power;
	}
}
