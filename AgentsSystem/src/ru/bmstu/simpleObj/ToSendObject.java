package ru.bmstu.simpleObj;

import jade.lang.acl.ACLMessage;
import ru.bmstu.ontology.Energy;

public class ToSendObject {

	public String conversId;
	public int performative;
	public Energy Energy;
	public ACLMessage reply;
	
	public int getPerformative() {
		return performative;
	}

	public void setPerformative(int performative) {
		this.performative = performative;
	}

	public Energy getEnergy() {
		return Energy;
	}

	public void setEnergy(Energy energy) {
		Energy = energy;
	}

	public ACLMessage getReply() {
		return reply;
	}

	public void setReply(ACLMessage reply) {
		this.reply = reply;
	}

	public String getConversId() {
		return conversId;
	}

	public void setConversId(String conversId) {
		this.conversId = conversId;
	}

}
