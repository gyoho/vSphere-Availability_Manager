 /**
   * Responsibility: handle the case for VM failure 
   *
   * @param instance: VM
   * @return none: use try/catch to catch exception
   * 
   * Implementation: recover the VM with two different
   * 				 scenarios distinguished by its power state
   *
   */


package Components;

import java.rmi.RemoteException;
import java.util.ArrayList;

import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class VMFailureHandler implements Runnable {
	
	private int waitTime = 60*1000;
	private VirtualMachine vm;
	private ArrayList<ManagedEntity> vmList;
	
	public VMFailureHandler(VirtualMachine vm, ArrayList<ManagedEntity> vmList) {
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
		
		/** Check VM power state **/
		
		// Case1: power is off
		if(!PowerStateChecker.isPowerOn(vm)) {

			/** check alarm state **/
			
			// Case1.1: alarm is on: on purpose
            if(AlarmChecker.isTriggered(vm)) {
            	System.out.println("VM powered off by user");
            } 
            
            // Case1.2: alarm is off: failure happened
            else {
            	System.out.println("Check if alarm is set");
            	// power on the VM
                PowerOnTasker.powerOnVM(vm, waitTime);
            }
		}

		// Case2: power is on
		else {
			// failure: revert to the current snapshot
            RevertToSnapshotTasker.revertToCurrentSnapshot(vm);

            // power on the VM
            PowerOnTasker.powerOnVM(vm, waitTime);
		}
		
		// add the removed trouble VM back to the list
		// so it can start ping it again
		vmList.add(vm);
	}
}
