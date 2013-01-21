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

public class PowerplantBehaviour extends TickerBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int powerMass;
	private double spheat;
	private double efficiency2;
	private double efficiency1;
	private double current_power;
	private double assigned_power;
	private SubscriptionResponder dfSubscriptionResponder;
	private StateSubscriptionManager subManager;
	
	private double transitionDelta;
	private boolean isInTransition;

	public PowerplantBehaviour(Agent a, int Mass, double heat, double eff2, double eff1, StateSubscriptionManager subManager, SubscriptionResponder dfSubscriptionResp) {
		super(a, 5000);
		this.spheat=heat;
		this.efficiency2=eff2;
		this.efficiency1=eff1;
		this.powerMass=Mass;
		current_power=max_power_calc();
		this.assigned_power=this.current_power;
		this.subManager=subManager;
		this.dfSubscriptionResponder=dfSubscriptionResp;
		this.isInTransition = false;
		
		this.subManager.setPowerplant(this);
		
		try {
			MessageTemplate template = MessageTemplate.and(
					MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST),
					MessageTemplate.MatchPerformative(ACLMessage.REQUEST));

			myAgent.addBehaviour(new PowerplantRequestResponderBehaviour(this, this.myAgent, template));        
			System.out.println("Registered a Request Responder");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public boolean setAssignedPower(double requested_assigned_power) {
		if(this.check_assign(requested_assigned_power)) {
			this.assigned_power = requested_assigned_power;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onTick() {
		System.out.print("Current Power: ");
		System.out.println(this.current_power);		
		System.out.print("Assigned_power: ");
		System.out.println(this.assigned_power);
		System.out.print("Time Delay: ");
		System.out.println(this.time_delay());
		if(isInTransition) {
			System.out.println("Perfoming a transition step");
			if(current_power != assigned_power) {
				current_power -= transitionDelta;
				if (Math.abs(current_power - assigned_power) < transitionDelta) {
					current_power = assigned_power; 
					isInTransition = false;
					transitionDelta = 0;
				}
			} else {
				isInTransition = false;
				transitionDelta = 0;
			}
			
			if(!isInTransition) {
				System.out.println("Transition to new power value completed");
			}
		}
	}

	public void change_power() {
		double delta = (current_power - assigned_power) / time_delay();
		
		transitionDelta = delta;
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

	public double max_power_calc ()
	{
		//double swept_area=this.length*this.length*Math.PI;
		return this.powerMass*0.278*this.spheat*this.efficiency1*this.efficiency2;
	}

	public double time_delay() {
		double delta=Math.abs(10*(current_power-assigned_power)/max_power_calc());
		if (delta<0) return 1;
		else return delta;
	}

	public boolean check_assign(double requested_assigned_power){
		if (requested_assigned_power > this.max_power_calc() 
				|| requested_assigned_power < 0) {
			return false;
		} else {
			return true;
		}
	}
	
	public State getState() {
		State state = new State();
		state.setCurrentPower(this.current_power);
		state.setMaxPower(this.max_power_calc());
		state.setState((this.current_power > 0));
		return state;
	}
	
	public double getCurrentPower() {
		return current_power;
	}
}
