package behaviours;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SearchBlocksBehaviour extends OneShotBehaviour {
	/**
	 * Behavior for HPS block controller agent
	 */
	private static final long serialVersionUID = -8504231259656437627L;

	@Override
	public void action() {
		//Search the yellow pages for HPS blocks to control
		ServiceDescription ssd=new ServiceDescription();
		ssd.setName("hpsBlock"); //"hpsBlock" MUST be set as a service name by hpsBlock agent
		ssd.setType("power-generator"); //"power-generator" MUST be set as a service name by hpsBlock agent
		DFAgentDescription template=new DFAgentDescription();
		template.addServices(ssd);
		try {
				DFAgentDescription[] searchResult=DFService.search(myAgent, template);
				if (searchResult.length>0){
					/**
					 * hpsBlocks found
					 * requesting their parameters
					 */
					AID[] ids = new AID[searchResult.length];
					for (int i = 0; i < searchResult.length; ++i) { 
						ids[i]=searchResult[i].getName();
					}
					myAgent.addBehaviour(new RequestBlockParamsBehaviour(ids));
				}
				else{
					System.out.println("No hpsBlcok agents were found.");
				}
		} catch (FIPAException e) {
				e.printStackTrace();
		}
	}	
}
