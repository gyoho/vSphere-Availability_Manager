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
import java.util.ArrayList;

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
	
	private ArrayList<ManagedEntity> vmListInRecPool;
	private ArrayList<ManagedEntity> vmList;
	
	public TakeSnapshotTasker(ArrayList<ManagedEntity> vmListInRecPool, ArrayList<ManagedEntity> vmList) {
		this.vmListInRecPool = vmListInRecPool;
		this.vmList = vmList;
	}
	
	private void takeSnapshot(ManagedEntity vm) throws Exception, VmConfigFault, 
			SnapshotFault, TaskInProgress, FileFault, InvalidState, RuntimeFault, RemoteException {
		
		// RemoveAllSnapshots: Remove all old snapshots
		Task removeTask = ((VirtualMachine) vm).removeAllSnapshots_Task();      
		if(removeTask.waitForTask() == Task.SUCCESS) {
			System.out.println("Removed all snapshots for " + vm.getName());
		}


		// CreateSnapshot: Take the current state snapshot
		Task createTask = ((VirtualMachine) vm).createSnapshot_Task("latest snapshot", "remove all old snapshots", false, false);
		if (createTask.waitForTask() == Task.SUCCESS) {
			System.out.println("Snapshot was created for " + vm.getName());
		}
	}

	public void takeSnapshotForAll() {
		
		// take snapshot for all hostVMs
		for(ManagedEntity hostVM : vmListInRecPool) {
			try {
				takeSnapshot(hostVM);
			} catch (VmConfigFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SnapshotFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TaskInProgress e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidState e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		// take snapshot for all VMs
		for(ManagedEntity vm : vmList) {
			try {
				takeSnapshot(vm);
			} catch (VmConfigFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SnapshotFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TaskInProgress e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidState e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RuntimeFault e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
