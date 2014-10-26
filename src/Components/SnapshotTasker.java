package Components;

import java.util.ArrayList;

import com.vmware.vim25.mo.ManagedEntity;

public class SnapshotTasker extends TakeSnapshotTasker implements Runnable{
	
	private int sleepTime;

	public SnapshotTasker(ArrayList<ManagedEntity> vmListInRecPool, ArrayList<ManagedEntity> vmList) {
		super(vmListInRecPool, vmList);
		sleepTime = 10*60*1000;
	}
	
	@Override
	public void run() {
		
		do {
			takeSnapshotForAll();
		} while(wakeUp());
	}
	
	private boolean wakeUp() {
		// sleep 10 minutes
		try {
			Thread.sleep(sleepTime);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
		System.out.println("Snapshot tasker goes sleep...");
		return true;
	}

}
