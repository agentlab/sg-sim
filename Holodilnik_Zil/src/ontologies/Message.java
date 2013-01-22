package ontologies;

import jade.content.Concept;

public class Message implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recipient;
	private Electricity content_el;
	
	private String subject;
	
	
	public String getRecipient() {
		return this.recipient;
	}
	
	public String getSubject() {
		return this.subject;
	}
	
	public Electricity getContentElectro() {
		return this.content_el;
	}
	
	
	
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public void setContent(Electricity content) {
		this.content_el = content;
	}
	
	
}
