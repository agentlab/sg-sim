package Ontologies;

import jade.content.Concept;

public class Message implements Concept {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private String Action;
	
	
	public String getRecipient() {
		return this.Action;
	}
}
