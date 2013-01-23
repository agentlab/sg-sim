package message;

import jade.content.Predicate;

public class ResponceAction implements Predicate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3578630536689637349L;
	//
	private boolean state;
	private double percent;
	
	public boolean isState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
	public double getPercent() {
		return percent;
	}
	public void setPercent(double percent) {
		this.percent = percent;
	}

}
