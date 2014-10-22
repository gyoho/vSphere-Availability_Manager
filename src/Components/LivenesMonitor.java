 /**
   * Responsibility: check VM liveness
   *
   * @param instance: vHost, VM
   * @param interval: ping interval --> The timeout value, in milliseconds
   *                    indicates the maximum amount of time the try should take.
   * @return dead: boolean  [reduce dependency instead notifying recovery manager]
   *
   * Implementation: ping the VM regularly with the interval provided
   *
   */

package Components;

import java.net.InetAddress;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class LivenesMonitor {
    
    private int timeout;
    private boolean isReachable = false;
    
    public LivenesMonitor(int timeout) {
        this.timeout = timeout;
    }
    
    void setTimeout(int timeout) {
        this.timeout = timeout;
    }
	
	public boolean isReachable(ManagedEntity instance) throws Exception {
		
		// get IP address
		String ipAddr = ((VirtualMachine)instance).getGuest().getIpAddress();
		System.out.println("IP address: " + ipAddr);
		
		if(ipAddr == null) {
			// this is not reachable. b/c the ip is not available when power off
			// consider this as dead
			return isReachable;
		}
		
		// Ping the VM
		InetAddress inetVM = InetAddress.getByName(ipAddr);
		isReachable = inetVM.isReachable(timeout);
		System.out.println(isReachable);
		return isReachable;
	}

}
