package ontologies;

import jade.content.Concept;

public class TimeDelay implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double time;

	public double getTimeDelay() {
		return this.time;
	}


	public void setTime(double time) {
		this.time=time;
	}

}