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

import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

public class HostFailureHandler implements FailureHandler {
	
	private int waitTime = 60*1000;
	private VirtualMachine hostVM;
	private HostSystem host;
	
	public HostFailureHandler(VirtualMachine hostVM, HostSystem host) {
		this.hostVM = hostVM;
		this.host = host;
	}

	@Override
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
