 /**
   * Responsibility: get the service instance with the IP address
   *
   * @param rootFolder
   * @param String ResourcePoolName
   * @return ResourcePool resourcePool: specific resource pool for the name given
   *
   */


package Components;

import java.rmi.RemoteException;
import java.util.ArrayList;

import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ResourcePool;

public class ResourcePoolGetter {
	
	public static ManagedEntity getResourcePool(Folder rootFolder, String ResourcePoolName) throws InvalidProperty, RuntimeFault, RemoteException {
		
		ArrayList<ManagedEntity> resourcePoolList;
		resourcePoolList = InstanceGetter.getAllInstance(rootFolder, "ResourcePool");
		
		for(ManagedEntity resourcePool : resourcePoolList) {
			if(((ResourcePool)resourcePool).getName() == ResourcePoolName) {
				return resourcePool;
			}			
		}
		
		System.out.println("No resource pool for that name found");
		return null;		
	}
	
	
}
