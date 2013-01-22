package ru.bmstu.protocols;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.AchieveREInitiator;

public class BrokeringInitiator extends Behaviour {

		public BrokeringInitiator(Agent myAgent, ACLMessage msg) 
		{
		
		}
		
	public ACLMessage handleConfirm(ACLMessage msg)
	{
		return msg;
		
	}
	
	public void prepareRequest(ACLMessage msg)
	{
		
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
