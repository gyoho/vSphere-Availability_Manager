/**
  * Responsibility: power on the instance
  *
  * @param instance: vHost, VM
  * @return none: use try/catch to catch exception
  *
  */


package Components;

import java.rmi.RemoteException;

import com.vmware.vim25.FileFault;
import com.vmware.vim25.InsufficientResourcesFault;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class PowerOnTasker {
	
	public static void powerOn(ManagedEntity vm) throws RuntimeFault, RemoteException, InterruptedException {
		// Power On
		Task powerOnTask = ((VirtualMachine)vm).powerOnVM_Task(null);
		
		if (powerOnTask.waitForTask() == Task.SUCCESS) {
			System.out.println("vm:" + vm.getName() + " powered on.");
			
			// after power on, need time to boot up the VM
			Thread.sleep(60*1000);
		} else {
			System.out.println("Hardware failure");
		}
	}

}
