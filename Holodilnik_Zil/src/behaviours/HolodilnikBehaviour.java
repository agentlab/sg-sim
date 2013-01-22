package behaviours;

import jade.core.Agent;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionResponder;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import ontologies.*;

import java.lang.Math;

public class HolodilnikBehaviour extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double max_temp;
	private double min_temp;
	private double chill_delta;
	private double W_hh;
	private double W_on;
	private double W_off;
	private double current_temperature;
	private double assigned_temperature;
	private SubscriptionResponder dfSubscriptionResponder;
	private StateSubscriptionManager subManager;
	
	
	private boolean isInTransition;

	public HolodilnikBehaviour(Agent a, double max_temp, double min_temp, double chill_delta, double W_hh, double W_on, double W_off, StateSubscriptionManager subManager, SubscriptionResponder dfSubscriptionResp) {
		super(a, 5000);
		this.max_temp=max_temp;
		this.min_temp=min_temp;
		this.chill_delta=chill_delta;
		this.W_hh=W_hh;
		this.W_on=W_on;
		this.W_off=W_off;
		current_temperature=10;
		this.assigned_temperature=this.current_temperature;
		this.subManager=subManager;
		this.dfSubscriptionResponder=dfSubscriptionResp;
		this.isInTransition = false;
		
		this.subManager.setHolodilnik(this);
		
		try {
			MessageTemplate template = MessageTemplate.and(
					MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

			myAgent.addBehaviour(new HolodilnikRequestResponderBehaviour(this, this.myAgent, template));        
			System.out.println("Registered a Request Responder");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean setAssignedTemperature(double requested_assigned_temperature) {
		if(this.check_assign(requested_assigned_temperature)) {
			this.assigned_temperature = requested_assigned_temperature;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onTick() {
		System.out.print("Current temperature: ");
		System.out.println(this.current_temperature);		
		System.out.print("Assigned_temperature: ");
		System.out.println(this.assigned_temperature);
		System.out.print("Time Delay: ");
		System.out.println(this.time_delay());
		if(isInTransition) {
			System.out.println("Perfoming a transition step");
			if(current_temperature != assigned_temperature) {
				if (current_temperature > assigned_temperature)
					current_temperature -= chill_delta;
				else current_temperature += chill_delta;
				if (Math.abs(current_temperature - assigned_temperature) < chill_delta) {
					current_temperature = assigned_temperature; 
					isInTransition = false;
					
				}
			} else {
				isInTransition = false;
				
			}
			
			if(!isInTransition) {
				System.out.println("Transition to new temperature value completed");
			}
		}
	}

	public void change_temperature() {
		
		isInTransition = true;
		
		System.out.println("Entering a transient state");
		/*
		while (current_power != assigned_power) {
			current_power -= delta;
			if (Math.abs(current_power - assigned_power) < delta) {
				current_power = assigned_power; 
				break;
			}
			try {
				for(int i = 5000; i > 0; i -= 100) {
					Thread.yield();
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
	}

	

	public double time_delay() {
		double delta=Math.abs(5*(current_temperature-assigned_temperature)/this.chill_delta);
		if (delta<0) return 1;
		else return delta;
	}

	public boolean check_assign(double requested_assigned_temperature){
		if (requested_assigned_temperature > this.max_temp 
				|| requested_assigned_temperature < this.min_temp) {
			return false;
		} else {
			return true;
		}
	}
	
	public double getCurrentTemperature() {
		
		
		return this.current_temperature;
	}
	
	public double getCurrentPower() {
		if(this.current_temperature>this.assigned_temperature)
			return this.W_on;
		else 
			if(this.current_temperature<this.assigned_temperature)
				return this.W_off;
			else
				return this.W_hh;
	}
}
