package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class TSAgent extends Agent {
	protected void setup() {
		System.out.println("TSAgent "+getAID().getName()+" is ready.");
		
		addBehaviour(new Receive());
	}
	private class Receive extends CyclicBehaviour {

		@Override
		public void action() {
			// TODO Auto-generated method stub
			ACLMessage sumEn=myAgent.receive();
			if (sumEn!=null) {
				String volume=sumEn.getContent();
				System.out.println("Summary consumering energy= "+volume);
			}
			else {
				block();
			}
		}
		
	}
}
