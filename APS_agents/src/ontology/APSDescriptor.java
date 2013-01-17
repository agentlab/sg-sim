package ontology;

import jade.content.Predicate;
import jade.core.AID;

public class APSDescriptor implements Predicate {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1088316555701498765L;
	private AID APSId;
	private double availP; //доступная мощность
	
	
	public APSDescriptor() {
		this.APSId=null;
		this.availP=0;
	}

	public APSDescriptor(AID APSId, double availP) {
		super();
		this.APSId = APSId;
		this.availP = availP;
	}

	public AID getAPSId() {
		return APSId;
	}

	public void setAPSId(AID APSId) {
		this.APSId = APSId;
	}

	public double getAvailP() {
		return availP;
	}

	public void setAvailP(double availP) {
		this.availP = availP;
	}
}
