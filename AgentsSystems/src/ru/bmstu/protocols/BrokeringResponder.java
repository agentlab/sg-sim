package ru.bmstu.protocols;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

public class BrokeringResponder extends Behaviour{
	
	public BrokeringResponder(Agent myAgent) 
	{
	
	}
	
	public ACLMessage handleRequest(ACLMessage msg)
	{
		return msg;
		
	}
	
	public void  handleAgree(ACLMessage msg)
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
