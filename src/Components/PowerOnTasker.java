/**
  * Responsibility: power on the instance
  *
  * @param instance: vHost, VM
  * @return none: use try/catch to catch exception
  *
  */


package Components;

import java.rmi.RemoteException;
import java.util.ArrayList;

import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.VirtualMachinePowerState;
import com.vmware.vim25.VirtualMachineRuntimeInfo;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class PowerOnTasker {
	
	public static void powerOnVM(VirtualMachine vm, long waitTime) throws RuntimeFault, RemoteException, InterruptedException {
		
		VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm.getRuntime();
		
		if(vmri.getPowerState() == VirtualMachinePowerState.poweredOn) {
			// Power On
			Task powerOnTask = ((VirtualMachine)vm).powerOnVM_Task(null);
			
			if (powerOnTask.waitForTask() == Task.SUCCESS) {
				System.out.println("vm:" + vm.getName() + " powered on.");
				
				// after power on, need time to boot up the VM
				Thread.sleep(waitTime);
			}
			else {
				System.out.println("Hardware failure");
			}
		}
	}
	
	public static void powerOnAllVMInHost(HostSystem host) throws RuntimeFault, RemoteException, InterruptedException {
		
		// get all the VMs for the host
		ArrayList<ManagedEntity> vmList = VmToHostMapper.getAllVMsOfHost(host);
		
		// for each vm in the list, register it to the host
		for(ManagedEntity vm : vmList) {
			powerOnVM((VirtualMachine) vm, (long) (0.1*1000));
		}
	}
}
