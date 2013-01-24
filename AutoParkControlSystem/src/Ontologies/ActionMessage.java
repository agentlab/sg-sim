package Ontologies;

import jade.content.Concept;

public class ActionMessage implements Concept {

	private static final long serialVersionUID = 1L;
    private int Action;
	
    public int getAction() {
		return this.Action;
	}
    
	public void setAction(int action) {
		this.Action = action;
	}
}
