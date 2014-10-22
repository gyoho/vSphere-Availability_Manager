 /**
   * Responsibility: take snapshot for the instance
   *
   * @param instance: vHost, VM
   * @return none: use try/catch to catch exception
   *
   * Implementation: only keep the latest snapshot
   */

package Components;

import java.rmi.RemoteException;

import com.vmware.vim25.FileFault;
import com.vmware.vim25.InvalidState;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SnapshotFault;
import com.vmware.vim25.TaskInProgress;
import com.vmware.vim25.VmConfigFault;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.Task;
import com.vmware.vim25.mo.VirtualMachine;




public class TakeSnapshotTasker {
	
	public static void takeSnapshot(ManagedEntity vm) throws Exception, VmConfigFault, 
			SnapshotFault, TaskInProgress, FileFault, InvalidState, RuntimeFault, RemoteException {
		
		// RemoveAllSnapshots: Remove all old snapshots
		Task removeTask = ((VirtualMachine) vm).removeAllSnapshots_Task();      
		if(removeTask.waitForTask() == Task.SUCCESS) {
			System.out.println("Removed all snapshots");
		}


		// CreateSnapshot: Take the current state snapshot
		Task createTask = ((VirtualMachine) vm).createSnapshot_Task("latest snapshot", "remove all old snapshots", false, false);
		if (createTask.waitForTask() == Task.SUCCESS) {
			System.out.println("Snapshot was created.");
		}
	}

}
