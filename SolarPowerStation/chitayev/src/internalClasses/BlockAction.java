package internalClasses;

import jade.core.AID;
import ontology.TPPblockControlAction;

public class BlockAction {
	public AID blockId;
	public TPPblockControlAction action;
	
	public BlockAction(AID blockId, TPPblockControlAction action) {
		this.blockId = blockId;
		this.action = action;
	}
}
