package Components;

import java.util.ArrayList;

import com.vmware.vim25.mo.ManagedEntity;

public class RecoveryManager implements Runnable {
	
	private ArrayList<ManagedEntity> vmList;
	private ArrayList<ManagedEntity> hostList;
	private ArrayList<ManagedEntity> vmListInRecPool;
	private FailureMonitor failureMonitor;
	
	public RecoveryManager(ArrayList<ManagedEntity> vmListInRecPool, ArrayList<ManagedEntity> hostList, ArrayList<ManagedEntity> vmList) {
		this.vmList = vmList;
		this.hostList = hostList;
		this.vmListInRecPool = vmListInRecPool;
		failureMonitor = new FailureMonitor();
	}

	@Override
	public void run() {
		
		while(true) {
			// monitor the VM by pinging
			// instantiate a handler according to the case
			try {
				failureMonitor.start(vmListInRecPool, hostList, vmList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
