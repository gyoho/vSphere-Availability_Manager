package Components;

import java.rmi.RemoteException;

import com.vmware.vim25.RuntimeFault;


public interface FailureHandler {
	
	// run process according to the handler type
	public void process() throws RuntimeFault, RemoteException, InterruptedException;
	
}
