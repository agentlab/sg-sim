package ontology;

import jade.content.Predicate;
import jade.core.AID;

public class HPSDescriptor implements Predicate {

	/**
	 * 
	 */
	private static final long serialVersionUID = -615284809681884483L;
	private AID hpsId;
	private double availP; //availible Power
	
	
	public HPSDescriptor() {
		this.hpsId=null;
		this.availP=0;
	}

	public HPSDescriptor(AID hpsId, double availP) {
		super();
		this.hpsId = hpsId;
		this.availP = availP;
	}

	public AID getHpsId() {
		return hpsId;
	}

	public void setHpsId(AID hpsId) {
		this.hpsId = hpsId;
	}

	public double getAvailP() {
		return availP;
	}

	public void setAvailP(double availP) {
		this.availP = availP;
	}
}
