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
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.VirtualMachine;

public class VMFailureHandler implements FailureHandler{
	
	private int waitTime = 60*1000;
	private VirtualMachine vm;
	
	public VMFailureHandler(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
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
	}
}
