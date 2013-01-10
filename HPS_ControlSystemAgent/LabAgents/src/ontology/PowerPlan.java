package ontology;

import java.util.Date;

import jade.content.Predicate;

public class PowerPlan implements Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1906565578457176000L;
	private double[] plan;
	private Date date;
	
	
	public PowerPlan() {
		this.plan=new double[24];
		this.date=new Date();
	}
	public PowerPlan(double[] plan, Date date) {
		this.plan = plan;
		this.date = date;
	}
	public double[] getPlan() {
		return plan;
	}
	public void setPlan(double[] plan) {
		this.plan = plan;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public void setPower(double p, int i){
		this.plan[i]=p;
	}
}
