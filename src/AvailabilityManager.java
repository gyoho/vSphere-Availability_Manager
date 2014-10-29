import java.util.ArrayList;
import java.util.Arrays;

import com.vmware.vim25.mo.*;

import Components.*;


public class AvailabilityManager {
	
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
		
		
		// set alarm for all the datacenter
		for(ManagedEntity dc : datacenterList) {
			CreateVmPowerStateAlarm.registerAlarm(vCenterServiceInstance, dc);
		}
		
		// Alternative way: set alarm for all VMs
		/*for(ManagedEntity vm : vmList) {
			CreateVmPowerStateAlarm.registerAlarm(vCenterServiceInstance, vm);
		}*/
		
	}
	
	public void start() {
		
		// print statistics
		StatisticsManager.printStats(vmList);
			
		/** Main Starts **/
		try {
			
			// take snapshot for every configured time
			new Thread(new SnapshotTasker(vmListInRecPool, vmList)).start();
			
			// for the first snapshot wait to finish the job
			Thread.sleep(2*1000);
			
			// get the recovery manager work
			new Thread(new RecoveryManager(vmListInRecPool, hostList, vmList)).start();
			
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
	}
}
