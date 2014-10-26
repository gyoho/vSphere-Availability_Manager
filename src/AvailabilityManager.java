import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;

import com.vmware.vim25.mo.*;

import Components.*;


public class AvailabilityManager extends Frame {
	
	private Thread recoveryManager;
	
	
	
	private CredentialsHolder credentials;
	
	// for vCenter
	private ServiceInstance vCenterServiceInstance;
	private Folder rootFolderOfvCenter;
	
	// for mgmt center
	private ServiceInstance mgmtCenterServiceInstance;
	private Folder rootFolderOfMgmtCenter;
	private ManagedEntity resourcePool;
	
	// all instances
	private ArrayList<ManagedEntity> vmList;
	private ArrayList<ManagedEntity> hostList;
	private ArrayList<ManagedEntity> datacenterList;
	private ArrayList<ManagedEntity> vmListInRecPool;
	
	// main tasker
//	private VmToHostMapper vmToHostMapper;	
	private SnapshotTasker snapshotTasker;

	
	// constructor
	public AvailabilityManager() throws Exception {
		
		/*** All initial configuration ***/
		
		// Instantiate credentials
		credentials = new CredentialsHolder();
		
		// get service instance in vCenter
		vCenterServiceInstance = ServiceInstanceGetter.getServiceInstance
				(credentials.getvCenterIpAddr(), credentials.getUsrName(), credentials.getPasswd());
		// get root folder of the vCenter
		rootFolderOfvCenter = RootFolderGetter.getRootFolder(vCenterServiceInstance);
		// get all VMs in the vCenter, excluding hosts
		vmList = InstanceGetter.getAllInstance(rootFolderOfvCenter, "VirtualMachine");
		// get all hosts
		hostList = InstanceGetter.getAllInstance(rootFolderOfvCenter, "HostSystem");
		// get all datacenters
		datacenterList = InstanceGetter.getAllInstance(rootFolderOfvCenter, "Datacenter");
		

		// get service instance in Management center
		mgmtCenterServiceInstance = ServiceInstanceGetter.getServiceInstance
				(credentials.getManagementCenterIpAddr(), credentials.getUsrName(), credentials.getPasswd());
		// get root folder of the management center
		rootFolderOfMgmtCenter = RootFolderGetter.getRootFolder(mgmtCenterServiceInstance);		
		// get resource pool for the given name
		resourcePool = ResourcePoolGetter.getResourcePool(rootFolderOfMgmtCenter, credentials.getResourcePoolName());
		// get all VMs in the resource pool
		vmListInRecPool = new ArrayList<ManagedEntity>(Arrays.asList(((ResourcePool)resourcePool).getVMs()));
		
		
		/*// instantiate the vm to host map
		vmToHostMapper = new VmToHostMapper(hostList, vmListInRecPool);*/
		
		// set alarm for all the datacenter
		for(ManagedEntity dc : datacenterList) {
			CreateVmPowerStateAlarm.registerAlarm(vCenterServiceInstance, dc);
		}
		
		// Alternative way: set alarm for all VMs
		/*for(ManagedEntity vm : vmList) {
			CreateVmPowerStateAlarm.registerAlarm(vCenterServiceInstance, vm);
		}*/
		
		// instantiate the Snapshot Taker
//		snapshotTasker = new SnapshotTasker(vmListInRecPool, vmList);
		
		// take first snapshot
//		TODO: activate
//		snapshotTasker.takeSnapshot();
		
		
		recoveryManager = new Thread(new RecoveryManager(vmListInRecPool, hostList, vmList));
		
		
		
		addKeyListener (new keyDown());
		
	}
	
	public void start() throws Exception {
		
		
		/*for(ManagedEntity vm : vmList) {
			System.out.println("vm name: " + vm.getName());
			String name = vmToHostMapper.getHostOfVm(vm).getName();
			System.out.println("host name: " + name);
		}*/
		
		while(true) {
			
			// print statistics
			
						
			// get the recovery manager work
//			new Thread(new RecoveryManager(vmList, vmToHostMapper)).start();
			recoveryManager.start();
			
			
			// take snapshot for every configured time
//			new Thread(new SnapshotTasker(vmListInRecPool, vmList)).start();
			
		}
	}
	
	// inner class for intercepting the key press
	private class keyDown extends KeyAdapter {
		public void keyPressed (KeyEvent e) {
			int keyCode = e.getKeyCode();
			switch(keyCode) {
				case KeyEvent.VK_Q:
					System.exit(0);
			}
		}
	}
}
