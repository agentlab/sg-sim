package internalClasses;

import jade.core.AID;
import ontology.APSblockControlAction;

public class BlockAction {
	public AID blockId;
	public APSblockControlAction action;
	
	public BlockAction(AID blockId, APSblockControlAction action) {
		this.blockId = blockId;
		this.action = action;
	}
}
