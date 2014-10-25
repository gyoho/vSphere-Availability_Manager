 /**
   * Responsibility: revert to the current snapshot
   *
   * @param instance: vHost, VM
   * @return none: use try/catch to catch exception
   *
   */


package Components;

import java.rmi.RemoteException;

import com.vmware.vim25.InsufficientResourcesFault;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.NotFound;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SnapshotFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;

public class RevertToSnapshotTasker {
	
	public static void revertToCurrentSnapshot(ManagedEntity vm) throws VmConfigFault, SnapshotFault,
			TaskInProgress, InvalidState, InsufficientResourcesFault, NotFound, RuntimeFault, RemoteException, InterruptedException {
		
		// revert to current snapshot
		Task revertTask = ((VirtualMachine) vm).revertToCurrentSnapshot_Task(null);
		if (revertTask.waitForTask() == Task.SUCCESS) {
			System.out.println(vm.getName() + " is reverted to the latest snapshot.");
		}
	}

}
