import java.util.ArrayList;
import java.util.Arrays;

import Components.CreateVmPowerStateAlarm;
import Components.CredentialsHolder;
import Components.InstanceGetter;
import Components.LivenessChecker;
import Components.ResourcePoolGetter;
import Components.RootFolderGetter;
import Components.ServiceInstanceGetter;
import Components.VmToHostMapper;

import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;


public class MapperTest {
	
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
	
	// all helper
	private VmToHostMapper vmToHostMapper;	
	private LivenessChecker livenessMonitor;
	
	
	// constructor
	public MapperTest() throws Exception {
		
		// Instantiate credentials
		credentials = new CredentialsHolder();
		
		// get service instance in vCenter
		vCenterServiceInstance = ServiceInstanceGetter.getServiceInstance(credentials.getvCenterIpAddr(), credentials.getUsrName(), credentials.getPasswd());
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
		
		
		// instantiate the vm to host map
		vmToHostMapper = new VmToHostMapper(hostList, vmListInRecPool);
		
		// set alarm for the datacenter
		for(ManagedEntity dc : datacenterList) {
			CreateVmPowerStateAlarm.registerAlarm(vCenterServiceInstance, dc);
		}
	}
		
	public void test() {
		// for each VM in the resource pool
		// match the name of the last 8 chars with the VM's host name
		for(ManagedEntity vm : vmListInRecPool) {
			
			// get the VM's name
			String vmName = ((VirtualMachine)vm).getName();
//			System.out.println("host vm name: " + vmName);
			
			// get the last 8 chars of its IP address
			String lastIPAddr = vmName.substring(vmName.length()-8);
//			System.out.println("host vm name last 8 chars: " + lastIPAddr);
			
			// for each host, check if the host's name ends with the 8 chars
			for(ManagedEntity host : hostList) {
				
				// get the host's name
				String hostName = ((HostSystem)host).getName();
//				System.out.println("host name: " + hostName);
				
				// check if matches
				if(hostName.endsWith(lastIPAddr)) {
					/*** register the VMs of the host ***/
					// get all the VMs for the host
					
				}
			}
			
//			System.out.println("-----------------------------------");
		}
	}

}
