package behaviours;

import java.util.Date;

import ontology.PowerPlan;
import ontology.PowerProducerOntology;
import jade.content.lang.Codec.CodecException;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.OntologyException;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.FIPAAgentManagement.FailureException;
import jade.domain.FIPAAgentManagement.NotUnderstoodException;
import jade.domain.FIPAAgentManagement.RefuseException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionResponder.Subscription;
import jade.proto.SubscriptionResponder.SubscriptionManager;
import agents.PowerProducerAgent;

public class PowerPlansSubscriptionManager implements SubscriptionManager {
	private PowerProducerAgent producer;
	private final long NOTIFICATION_PERIOD=24*3000;

	public PowerPlansSubscriptionManager(PowerProducerAgent producer) {
		super();
		this.producer = producer;
	}
	@Override
	public boolean register(Subscription s) throws RefuseException,
	NotUnderstoodException {
		boolean result=false;
		if(s.getMessage().getContent().equalsIgnoreCase("power-plans-subscription")){
			producer.setPpSubscription(s);
			System.out.println(producer.getLocalName()+": received subscription for "+s.getMessage().getContent()+" from "+s.getMessage().getSender().getLocalName());
			//sending first power plan immediately
			this.notify(producer);
			//add behavior for sending one day notifications
			producer.addBehaviour(new TickerBehaviour(producer, NOTIFICATION_PERIOD) {
				private static final long serialVersionUID = 3674681447400834233L;
				@Override
				protected void onTick() {
					((PowerProducerAgent) myAgent).getPpSubMngr().notify((PowerProducerAgent) myAgent);
				}
			});
			//Subscribing for receiving production result each day
			producer.addBehaviour(new OneDayResultSubscriptionBehaviour(producer,s.getMessage().getSender()));
			result=true;
		}
		return result;
	}
	@Override
	public boolean deregister(Subscription s) throws FailureException {
		System.out.println("Agent "+s.getMessage().getSender().getLocalName()+" canceled subscription.");
		return true;
	}
	/**
	 * Notify the subscriber
	 * @param a
	 */
	public void notify(PowerProducerAgent a){
		ACLMessage notification;
		Subscription s=a.getPpSubscription();
		notification = s.getMessage().createReply();
		notification.setPerformative(ACLMessage.INFORM);
		notification.setLanguage(new SLCodec().getName());
		notification.setOntology(PowerProducerOntology.getInstance().getName());
		/**
		 * generate random power plan and send		
		 */
		double[] power=new double[24];
		for(int i=0; i<power.length;i++){
			power[i]=a.getControllerDescriptor().getAvailP()*Math.random();
		}
		Date date=new Date();
		PowerPlan nextDayPlan=new PowerPlan(power, date);
		try {
			a.getContentManager().fillContent(notification, nextDayPlan);
			s.notify(notification);
		} catch (CodecException e) {
			e.printStackTrace();
		} catch (OntologyException e) {
			e.printStackTrace();
		}		
	}
}
