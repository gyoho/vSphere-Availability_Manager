 /**
   * Responsibility: handle the case for host failure 
   *
   * @param instance: Host
   * @return none: use try/catch to catch exception
   * 
   * Implementation: recover the Host with two different
   * 				 scenarios distinguished by its power state
   *
   */

package Components;

import java.rmi.RemoteException;
import java.util.ArrayList;

import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class HostFailureHandler implements Runnable {
	
	private int waitTime = 60*1000;
	private VirtualMachine hostVM;
	private HostSystem host;
	private VirtualMachine vm;
	private ArrayList<ManagedEntity> vmList;
	
	public HostFailureHandler(VirtualMachine hostVM, HostSystem host, VirtualMachine vm, ArrayList<ManagedEntity> vmList) {
		this.hostVM = hostVM;
		this.host = host;
		this.vm = vm;
		this.vmList = vmList;
	}
	
	@Override
	public void run() {
		try {
			process();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void process() throws RuntimeFault, RemoteException, InterruptedException {
		
		/** check vHost power state **/
		
		// Case1: power is off (No need to check alarm)
		if(!PowerStateChecker.isPowerOn(hostVM)) {

			// take action immediately
			vHostPowerOnTask(hostVM, host);       
		}

		// Case2: power is on
		else {
			// vHost has problem, thus VM is also not reachable: take action

			// revert the vHost to the current snapshot
			RevertToSnapshotTasker.revertToCurrentSnapshot(hostVM);

			vHostPowerOnTask(hostVM, host);
		}
		
		// add the removed trouble VM back to the list
		// so it can start ping it again
		if(Thread.holdsLock(vm))
			vmList.add(vm);
	}
	
	private void vHostPowerOnTask(VirtualMachine hostVM, HostSystem host) throws RuntimeFault, RemoteException, InterruptedException {
		// power on the vHost
        PowerOnTasker.powerOnVM(hostVM, waitTime);
        
        // wait for a while
        ReconnectHostTasker.reconnect();

        // power on the all VMs belonging to the host
        PowerOnTasker.powerOnAllVMInHost(host);
	}
}
