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
	public enum BlockState{
		Active,
		Passive,
		Damage
	}
	private AID id;
	private double minP;
	private double maxP;
	private BlockState state; // possible states are active, passive, damage //may be used enum type for description
	
	public HPSblockDescriptor(){
		//set default values
		this.id=null;
		this.minP=100;
		this.maxP=1000;
		this.state=BlockState.Passive;
	}
	public HPSblockDescriptor(AID id, double minP, double maxP, BlockState state){
		this.id=id;
		this.minP=minP;
		this.maxP=maxP;
		this.state=state;		
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
	public BlockState getState() {
		return state;
	}
	public void setState(BlockState state) {
		this.state = state;
	}
}
