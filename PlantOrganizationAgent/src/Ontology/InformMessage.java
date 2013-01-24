package Ontology;

import jade.content.Predicate;

public class InformMessage implements Predicate{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371122000561102982L;
	
	private int price;
	private int volume;
	
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getVolume() {
		return volume;
	}
	public void setVolume(int volume) {
		this.volume = volume;
	}
	
}
