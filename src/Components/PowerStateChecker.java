 /**
   * Responsibility: check instance's power state
   *
   * @param instance: vHost, VM
   * @return isPowerOn: boolean
   *
   */

package Components;

import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class PowerStateChecker {
	
	public static boolean isPowerOn(ManagedEntity vm) {
		
		boolean isPowerOn = false;
		
		// get VM runtime status
		VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) ((VirtualMachine)vm).getRuntime();

		// get power status
		if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn) {
			isPowerOn = true;
		}
		
		return isPowerOn;		
	}

}
