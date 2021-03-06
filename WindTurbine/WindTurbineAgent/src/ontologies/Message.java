package ontologies;

import jade.content.Concept;

public class Message implements Concept {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String recipient;
	private Electricity content_el;
	private TimeDelay content_td;
	private State content_state;
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
	
	public TimeDelay getContentTD() {
		return this.content_td;
	}
	
	public State getContentState() {
		return this.content_state;
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
	public void setContent(State content) {
		this.content_state = content;
	}
	public void setContent(TimeDelay content) {
		this.content_td = content;
	}
	
}
