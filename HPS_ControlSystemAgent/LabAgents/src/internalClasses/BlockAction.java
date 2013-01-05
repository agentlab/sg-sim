package internalClasses;

import jade.core.AID;
import ontology.HPSblockControlAction;

public class BlockAction {
	public AID blockId;
	public HPSblockControlAction action;
	
	public BlockAction(AID blockId, HPSblockControlAction action) {
		this.blockId = blockId;
		this.action = action;
	}
}
