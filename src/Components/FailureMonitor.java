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

import java.util.ArrayList;

import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class FailureMonitor {
	
	private VirtualMachine troubleVM;
	
	public void start(ArrayList<ManagedEntity> vmListInRecPool, ArrayList<ManagedEntity> hostList, ArrayList<ManagedEntity> vmList) throws Exception {
	
		/** For each VM ping in turn**/
		// while all VMs are responding
		while(isAllVMsAlive(vmList)) {
			// do nothing
			// exit this loop when found trouble vm
		}
		
		/*** once the VM stops responding, start to identify the issue ***/
		
		// stop pinging the trouble VM
		// removing it from the list
		vmList.remove(troubleVM);
		
		
		/** check if its host is responding **/
		
		// get its host
		System.out.println("trouble vm name: " + troubleVM.getName());

		HostSystem host = (HostSystem) VmToHostMapper.getHostOfVm(hostList, troubleVM);
		System.out.println("host name: " + host.getName());
		
		// get its hostVM
		VirtualMachine hostVM = (VirtualMachine) VmToHostMapper.getHostVMOfVM(vmListInRecPool, hostList, troubleVM);
		
		// get the host IP address --> the host name = its IP address (static)
		String hostIpAddr = ((HostSystem)VmToHostMapper.getHostOfVm(hostList, troubleVM)).getName();
//		String hostIpAddr = host.getConfig().getNetwork().getVnic()[0].getSpec().getIp().getIpAddress();
		
		// Case1: VM down, vHost OK
		if(LivenessChecker.isResponding(hostIpAddr)) {
			System.out.println("\nVM is down\n");
			
			// instantiate the handler thread
			new Thread(new VMFailureHandler(troubleVM, vmList)).start();
		}
		
		// Case2: vHost down, thus VM also down too
		else {
			System.out.println("\nHost is down\n");
			
			// instantiate the handler thread
			new Thread(new HostFailureHandler(hostVM, host, troubleVM, vmList)).start();
		}	
	}
	
	// iterate through the VMs and ping each in turn 
	// until it finds a VM with trouble
	// at that time the vm variable will pointing the trouble VM

	private boolean isAllVMsAlive(ArrayList<ManagedEntity> vmList) throws Exception {
		for(ManagedEntity vm : vmList) {
			if(!LivenessChecker.isResponding((VirtualMachine)vm)) {
				troubleVM = (VirtualMachine) vm;
				return false;
			}
		}
		return true;
	}
	
	
	/*// Overload
	public static FailureHandler start(VirtualMachine hostVM, HostSystem host, VirtualMachine vm) throws Exception {
		
		// get the host IP address --> the host name = its IP address (static)

		String hostIpAddr = host.getConfig().getNetwork().getVnic()[0].getSpec().getIp().getIpAddress();
//		String hostIpAddr = ((HostSystem)mapper.getHostofVm(vm)).getName();
		
		return start(hostVM, hostIpAddr, vm);
	}*/
}
