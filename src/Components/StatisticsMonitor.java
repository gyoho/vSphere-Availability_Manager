 /**
   * Responsibility: get statistics
   *
   * @param instance: vHost, VM
   * @return: none --> print out the statistics (CPU, I/O, network)
   *
   */


package Components;

import com.vmware.vim25.VirtualMachineCapability;
import com.vmware.vim25.VirtualMachineConfigInfo;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class StatisticsMonitor {
	
	public void printStatistics(ManagedEntity vm) {
		   
		// get VM details
		VirtualMachineConfigInfo vminfo = ((VirtualMachine) vm).getConfig();
		VirtualMachineCapability vmc = ((VirtualMachine) vm).getCapability();
//		((VirtualMachine) vm).getResourcePool();


		System.out.println("Hello " + ((VirtualMachine) vm).getName());
		System.out.println("GuestOS: " + vminfo.getGuestFullName());
		System.out.println("Multiple snapshot supported: " + vmc.isMultipleSnapshotsSupported());
		System.out.println(((VirtualMachine) vm).getSummary().getGuest().getHostName());
	}

}
