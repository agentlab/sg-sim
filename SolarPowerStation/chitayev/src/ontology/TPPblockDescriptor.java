package ontology;

import jade.content.Predicate;
import jade.core.AID;

public class TPPblockDescriptor implements Predicate{

	private static final long serialVersionUID = 5295335826030436151L;
	public enum BlockState{
		Active,
		Passive
	}
	private AID id;
	private double minP;
	private double maxP;
	private BlockState state; // возможные состояни блока
	
	public TPPblockDescriptor(){
		//конструктор по умолчанию
		this.id=null;
		this.minP=10;
		this.maxP=100;
		this.state=BlockState.Passive;
	}
	public TPPblockDescriptor(AID id, double minP, double maxP, BlockState state){
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
