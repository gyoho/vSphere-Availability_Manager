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
import com.vmware.vim25.mo.VirtualMachine;

public class PowerOnTasker {
	
	public static void powerOn(ManagedEntity vm) {
		// Power On
		try {
			((VirtualMachine)vm).powerOnVM_Task(null);
		} catch (VmConfigFault e) {
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
		} catch (InsufficientResourcesFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\n\nvm:" + vm.getName() + " powered on.\n");
	}

}
