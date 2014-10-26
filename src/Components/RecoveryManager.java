package Components;

import java.rmi.RemoteException;

import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.VirtualMachine;

public class RecoveryManager implements Runnable {
	
	private VirtualMachine hostVM;
	private HostSystem host;
	private VirtualMachine vm;
	private FailureHandler failureHandler;
	
	public RecoveryManager(VirtualMachine hostVM, HostSystem host, VirtualMachine vm) {
		this.hostVM = hostVM;
		this.host = host;
		this.vm = vm;
		
	}

	@Override
	public void run() {
		// monitor the VM by pinging
		// instantiate a handler according to the case
		try {
			failureHandler = FailureMonitor.start(hostVM, host, vm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			failureHandler.process();
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
}
