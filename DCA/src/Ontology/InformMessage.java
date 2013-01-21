package Ontology;

import jade.content.Predicate;

public class InformMessage implements Predicate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371122000561102982L;
	
	private int dayReport;
	private int hourReport;
	private int volume;
	
	
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	public int getDayReport() {
		return dayReport;
	}
	public void setDayReport(int dayReport) {
		this.dayReport = dayReport;
	}
	public int getHourReport() {
		return hourReport;
	}
	public void setHourReport(int hourReport) {
		this.hourReport = hourReport;
	}
	
}
