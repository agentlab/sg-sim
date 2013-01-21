package message;

import jade.content.Predicate;

public class ChargerMessage implements Predicate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1441820980255593213L;
	//
	private double consumption;
	
	public double getConsumption() {
		return consumption;
	}
	public void setConsumption(double consumption) {
		this.consumption = consumption;
	}

}
