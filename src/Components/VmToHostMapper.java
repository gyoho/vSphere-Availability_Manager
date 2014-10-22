 /**
   * Responsibility: find the host for the VM
   *
   * @param vm: VM instance
   * @return host: host instance
   *
   */

package Components;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.HostSystem;
import com.vmware.vim25.mo.ManagedEntity;

public class VmToHostMapper {
	
	private HashMap<ManagedEntity, ManagedEntity> vmToHost;
	
	// constructor
	public VmToHostMapper(ArrayList<ManagedEntity> hostList) throws InvalidProperty, RuntimeFault, RemoteException {
		
		vmToHost = new HashMap<ManagedEntity, ManagedEntity>();
		
		// for each host, get all VMs belongs to the host
		for(ManagedEntity host : hostList) {
			
			ManagedEntity[] vmArray = ((HostSystem)host).getVms();
			ArrayList<ManagedEntity> vmList = new ArrayList<ManagedEntity>(Arrays.asList(vmArray));

			// for each vm in the list, register it to the host
			for(ManagedEntity vm : vmList) {
				vmToHost.put(vm, host);
			}
		}
	}
	
	public void registerVmToHost(ManagedEntity vm, ManagedEntity host) {
		vmToHost.put(vm, host);
	}
	
	public ManagedEntity getHostofVm(ManagedEntity vm) {
		ManagedEntity host = vmToHost.get(vm);
		return host;		
	}
}
