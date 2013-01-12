package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class Building extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7773294349902276869L;
	public AID[] consumers;				// объявление списка потребителей
	protected void setup() {
		System.out.println("Building agent "+getAID().getName()+" is ready.");
		addBehaviour(new ReceiveBehaviour());		// вызов поведения (создание поведния)
	}
	
	private class ReceiveBehaviour extends CyclicBehaviour {
		private MessageTemplate mt;
		private int msgCnt=0;
		private int volume;
		private int sumVol=0;
		@Override
		public void action() {
			// TODO Auto-generated method stub
			
			ACLMessage msg= myAgent.receive();
			if (msg!=null) {

				//searching conumers
				DFAgentDescription template = new DFAgentDescription();
				ServiceDescription sd = new ServiceDescription();
				sd.setType("energy-consumer");
				template.addServices(sd);
				try {
					DFAgentDescription[] result = DFService.search(myAgent, template); 
					System.out.println("Found the following consumer agents:");
					consumers = new AID[result.length];
					for (int i = 0; i < result.length; ++i) {
						consumers[i] = result[i].getName();
						System.out.println(consumers[i].getName());
					}
				}
				catch (FIPAException fe) {
					fe.printStackTrace();
				}
				
				volume=Integer.parseInt(msg.getContent());
				msgCnt=msgCnt+1;
				sumVol=sumVol+volume;
				if (msgCnt==consumers.length) {
					ACLMessage sum = new ACLMessage(ACLMessage.INFORM);
					sum.setContent(String.valueOf(sumVol));
					sum.addReceiver(new AID("TSAgent",AID.ISLOCALNAME));
					sum.setLanguage("English");
					send(sum);
					msgCnt=0;
					sumVol=0;
				}
			}
			else {
				block();
			}
		}
		
		
	}
}
