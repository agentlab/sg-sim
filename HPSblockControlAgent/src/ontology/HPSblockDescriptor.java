package ontology;

import jade.content.Predicate;
import jade.core.AID;

/**
 * 
 * @author Vasily Belolapotkov
 *
 */
/**
 * 
 * class describes parameters of the GPS block
 *
 */
public class HPSblockDescriptor implements Predicate{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7820587574620600301L;
	private AID id;
	private double minP;
	private double maxP;
	private String state; // possible states are active, passive, damage //may be used enum type for description
	private double wear; // wear of the block in percent
	
	public HPSblockDescriptor(){
		//set default values
		this.id=null;
		this.minP=100;
		this.maxP=1000;
		this.state="passive";
		this.wear=0;
	}
	public HPSblockDescriptor(AID id, double minP, double maxP, String state, double wear){
		this.id=id;
		this.minP=minP;
		this.maxP=maxP;
		this.state=state;
		this.wear=wear;			
	}
	public AID getId() {
		return id;
	}
	public void setId(AID id) {
		this.id = id;
	}
	public double getMaxP() {
		return maxP;
	}
	public void setMaxP(double maxP) {
		this.maxP = maxP;
	}
	public double getMinP() {
		return minP;
	}
	public void setMinP(double minP) {
		this.minP = minP;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public double getWear() {
		return wear;
	}
	public void setWear(double wear) {
		this.wear = wear;
	}
	
}
