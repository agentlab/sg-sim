package onto;

import jade.content.AgentAction;

public class SetMachineCount implements AgentAction
{
	private static final long serialVersionUID = 5281179776722803352L;
	//
	private int machine_count;
	
	/**
	 * @return the machine_count
	 */
	public int getMachine_count() {
		return machine_count;
	}
	/**
	 * @param machine_count the machine_count to set
	 */
	public void setMachine_count(int machine_count) {
		this.machine_count = machine_count;
	}
}
