package ontology;

import java.util.Date;

import jade.content.Predicate;



public class OneHourResult implements Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9158623544712229769L;
	private double power;
	private Date date;
	
	public OneHourResult(){
		this.power=0;
		this.date=null;
	}
	
	public OneHourResult(double power, Date date){
		this.power=power;
		this.date=date;
	}
	public double getPower() {
		return power;
	}
	public void setPower(double power) {
		this.power = power;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
