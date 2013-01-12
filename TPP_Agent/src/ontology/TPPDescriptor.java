package ontology;

import jade.content.Predicate;
import jade.core.AID;

public class TPPDescriptor implements Predicate {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1088316555701498765L;
	private AID tppId;
	private double availP; //доступная мощность
	
	
	public TPPDescriptor() {
		this.tppId=null;
		this.availP=0;
	}

	public TPPDescriptor(AID tppId, double availP) {
		super();
		this.tppId = tppId;
		this.availP = availP;
	}

	public AID getTppId() {
		return tppId;
	}

	public void setTppId(AID tppId) {
		this.tppId = tppId;
	}

	public double getAvailP() {
		return availP;
	}

	public void setAvailP(double availP) {
		this.availP = availP;
	}
}
