package Ontologies;

import jade.content.Concept;

public class PlanMessage implements Concept {

	private static final long serialVersionUID = 1L;
	private byte[] power;
	
	public byte[] getPower() {
		return this.power;
	}
	
	public void setPower(byte[] pwr) {
		this.power = pwr;
	}
}
