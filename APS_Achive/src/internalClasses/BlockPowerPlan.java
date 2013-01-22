package internalClasses;

import jade.core.AID;

public class BlockPowerPlan {
	public AID blockId;
	public double[] plan;
	public int i;
	
	
	public BlockPowerPlan() {
		this.blockId = null;
		this.plan=new double[24];
		this.i=0;
	}


	public BlockPowerPlan(AID blockId) {
		this.blockId = blockId;
		this.plan=new double[24];
		this.i=0;
	}
	
	
}
