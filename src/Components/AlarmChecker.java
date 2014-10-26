 /**
   * Responsibility: check if the alarm is triggered
   *
   * @param instance: vHost, VM
   * @return flag: boolean --> true if triggered
   *
   * Implementation: check the VM state. If yellow flag up, the alarm is triggered
   */


package Components;

import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class AlarmChecker {

	public static boolean isTriggered(ManagedEntity vm) {
		
		boolean isTriggered = false;
		
		if(((VirtualMachine)vm).getSummary().getOverallStatus().toString().equals("yellow")) {
			isTriggered = true;
			System.out.println("Alarm triggered!");
		} else {
			isTriggered = false;
			System.out.println("No Alarm triggered!");
		}
		
		return isTriggered;
	}
}
