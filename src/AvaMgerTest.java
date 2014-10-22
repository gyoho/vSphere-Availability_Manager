import java.net.URL;
import Components.*;
import com.vmware.vim25.mo.*;


public class AvaMgerTest {
	
	// all helper
	private VmToHostMapper vmToHostMapper;	
	private LivenesMonitor livenessMonitor;
	
	// specific center
	private ServiceInstance center;
	// specific Host
	private HostSystem host;
	// specific VM
	private VirtualMachine vm;
	// specific VM in mgmt center
	private VirtualMachine vmHost;
	
	// constructor
	public AvaMgerTest() throws Exception {
		
		// get center
		URL vCenterURL = new URL("https://130.65.132.106/sdk");
		center = new ServiceInstance(vCenterURL, "administrator", "12!@qwQW", true);
		Folder centerRootFolder = center.getRootFolder();

		// get specific VM
		String vmName = "T06-VM01-Ubt-Ypg";
		vm = (VirtualMachine) new InventoryNavigator(centerRootFolder).searchManagedEntity("VirtualMachine", vmName);

		// get Host
		String hostName = "130.65.132.182"; 
		host = (HostSystem) new InventoryNavigator(centerRootFolder).searchManagedEntity("HostSystem", hostName);
		
		// get center
		URL mgmtURL = new URL("https://130.65.132.14/sdk");
		ServiceInstance mgmtCenter = new ServiceInstance(vCenterURL, "administrator", "12!@qwQW", true);
		Folder mgmtRootFolder = mgmtCenter.getRootFolder();
		
		// get specific VM
		vmHost = (VirtualMachine) new InventoryNavigator(centerRootFolder).searchManagedEntity("VirtualMachine", hostName);
		
		// set alarm for all VMs
		CreateVmPowerStateAlarm.registerAlarmToVm(center, vm);
		
		// instantiate monitors for all VMs
		livenessMonitor = new LivenesMonitor(3*1000);
		
	}
	
	public void run() throws Exception {

		// monitor the VM by pinging
		
		// VM is not responding: identify who has problem
		if(!livenessMonitor.isReachable(vm)) {

			// check vHost (only start pinging vHost at this point)
		
			// Case1: VM down, vHost OK
			if(livenessMonitor.isReachable(vmHost)) {

				// Check VM power state
			
					// Case1: power is off
					if(!PowerStateChecker.isPowerOn(vm)) {
						
						// check alarm state: check if on purpose or by accident
						
						// Case1: alarm is on: on purpose
                        if(AlarmChecker.isTriggered(vm)) {
                        	// Do nothing
                        } 
                        
                        // Case2: alarm is off: failure happened (Cannot happen)
                        else {
                        	/* vHost is powered off */
                        	//TODO: recovery process
                        }
					}
	
					// Case2: power is on
					else {
						// failure: revert to the current snapshot
                        RevertToSnapshotTasker.revertToCurrentSnapshot(vm);
        
                        // power on the VM
                        PowerOnTasker.powerOn(vm);
					}
			}
		
			// Case2: vHost down, thus VM also down too
			else {
		
				// check vHost power state
		
				// Case1: power is off (No need to check alarm)
				if(!PowerStateChecker.isPowerOn(vmHost)) { 
		
					// take action immediately
        
                        // power on the vHost
                        PowerOnTasker.powerOn(vmHost);

                        // power on the VMs
                        PowerOnTasker.powerOn(vm);
				}

				// Case2: power is on
				else {
		
					// vHost has problem, thus VM is also not reachable: take action
		
						// revert the vHost to the current snapshot
						RevertToSnapshotTasker.revertToCurrentSnapshot(vmHost);
			
						// power on the vHost
	                    PowerOnTasker.powerOn(vmHost);
	
	                    // power on the VMs
	                    PowerOnTasker.powerOn(vm);
				}
			}
		}
	}
}

