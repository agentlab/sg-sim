package ontologies;

import jade.content.Concept;

public class Electricity implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double aver_power;
	private int time;


	public double getAverPower() {
		return this.aver_power;
	}

	public int getTime() {
		return this.time;
	}

	public void setAverPower(double aver_power) {
		this.aver_power=aver_power;
	}

	public void setTime(int time) {
		this.time=time;
	}

}