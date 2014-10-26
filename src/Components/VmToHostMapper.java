 /**
   * Responsibility: find the host for the VM
   *
   * @param vm: VM instance
   * @return host: ManagedEntity instance
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
import com.vmware.vim25.mo.VirtualMachine;

public class VmToHostMapper {
	
	private HashMap<ManagedEntity, ManagedEntity> vmToHost;
	private HashMap<ManagedEntity, ManagedEntity> vmToHostVM;
	
	// constructor
	public VmToHostMapper(ArrayList<ManagedEntity> hostList, ArrayList<ManagedEntity> vmListInRecPool) throws InvalidProperty, RuntimeFault, RemoteException {
		
		/** VM to vHost **/
		vmToHost = new HashMap<ManagedEntity, ManagedEntity>();
		
		// for each host, get all VMs belongs to the host
		for(ManagedEntity host : hostList) {
			// get all the VMs for the host
			ArrayList<ManagedEntity> vmList = getAllVMsOfHost(host);

			// for each vm in the list, register it to the host
			for(ManagedEntity vm : vmList) {
				vmToHost.put(vm, host);
			}
		}
		
		
		/** VM to HostVM **/
		vmToHostVM = new HashMap<ManagedEntity, ManagedEntity>();
		
		// for each VM in the resource pool
		// match the name of the last 8 chars with the VM's host name
		for(ManagedEntity hostVM : vmListInRecPool) {
			
			// get the VM's name
			String hostVMName = ((VirtualMachine)hostVM).getName();
//			System.out.println("host vm name: " + vmName);
			
			// get the last 8 chars of its IP address
			String lastIPAddr = hostVMName.substring(hostVMName.length()-8);
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
					ArrayList<ManagedEntity> vmList = getAllVMsOfHost(host);
					
					// for each vm in the list, register it to the host
					for(ManagedEntity vm : vmList) {
						vmToHostVM.put(vm, hostVM);
					}	
				}
			}		
		}	
	}
	
	// get all the VMs for the host
	public static ArrayList<ManagedEntity> getAllVMsOfHost(ManagedEntity host) throws InvalidProperty, RuntimeFault, RemoteException {
		ManagedEntity[] vmArray = ((HostSystem)host).getVms();
		return (new ArrayList<ManagedEntity>(Arrays.asList(vmArray)));
	}
	
	
	public void registerVmToHost(ManagedEntity vm, ManagedEntity host) {
		vmToHost.put(vm, host);
	}
	
	
	public void registerVmToHostVM(ManagedEntity vm, ManagedEntity hostVM) {
		vmToHostVM.put(vm, hostVM);
	}
	
	
	public ManagedEntity getHostOfVm(ManagedEntity vm) {
		ManagedEntity host = vmToHost.get(vm);
		return host;		
	}
	
	public ManagedEntity getHostVMOfVM(ManagedEntity vm) {
		ManagedEntity hostVM = vmToHostVM.get(vm);
		return hostVM;		
	} 
}
