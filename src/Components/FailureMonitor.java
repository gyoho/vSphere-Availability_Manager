 /**
   * Responsibility: identify where the issue happened
   * 				 and instantiate the handler accordingly 
   *
   * @param instance: VM
   * @return 
   * 
   * Implementation: 1. keep pinging the VM
   * 				 2. if it stops responding
   * 				 3. check if its host is responding
   */


package Components;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

public class FailureMonitor {
	
	public static FailureHandler start(VirtualMachine hostVM, HostSystem host, VirtualMachine vm) throws Exception {
		
		// while responding
		while(LivenessChecker.isResponding(vm)) {
			Thread.sleep(1*1000);
		}
		
		/*** once the VM stops responding, start to identify the issue ***/
		// check if the host responses
		
		// get the host IP address --> the host name = its IP address (static)
		String hostIpAddr = host.getConfig().getNetwork().getVnic()[0].getSpec().getIp().getIpAddress();
		
		// Case1: VM down, vHost OK
		if(LivenessChecker.isResponding(hostIpAddr)) {
			System.out.println("\nVM is down\n");
			return new VMFailureHandler(vm);
		}
		
		// Case2: vHost down, thus VM also down too
		else {
			System.out.println("\nHost is down\n");
			return new HostFailureHandler(hostVM, host);
		}	
	}
	
	
	/*// Overload
	public static FailureHandler start(VirtualMachine hostVM, HostSystem host, VirtualMachine vm) throws Exception {
		
		// get the host IP address --> the host name = its IP address (static)

		String hostIpAddr = host.getConfig().getNetwork().getVnic()[0].getSpec().getIp().getIpAddress();
//		String hostIpAddr = ((HostSystem)mapper.getHostofVm(vm)).getName();
		
		return start(hostVM, hostIpAddr, vm);
	}*/
}
