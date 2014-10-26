 /**
   * Responsibility: check VM liveness
   *
   * @param instance: vHost, VM
   * @param String: ipAddr
   * @param interval: ping interval --> The timeout value, in milliseconds
   *                    indicates the maximum amount of time the try should take.
   * @return dead: boolean  [reduce dependency instead notifying recovery manager]
   *
   * Implementation: ping the VM regularly with the interval provided
   *
   */

package Components;

import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class LivenessChecker {
    
    /*private int timeout;
    private boolean isReachable = false;
    
    public LivenessMonitor(int timeout) {
        this.timeout = timeout;
    }
    
    void setTimeout(int timeout) {
        this.timeout = timeout;
    }*/
    
    public static boolean isResponding(String ipAddr) throws Exception {
    	boolean isReachable = false;
    	
		String cmd = "ping -c 1 " + ipAddr;
		Process ping = Runtime.getRuntime().exec(cmd);
		ping.waitFor();
		if(ping.exitValue() == 0) {
			isReachable =  true;
		} else {
			isReachable = false;
		}
		
//		System.out.println(ipAddr + " reachable: " + isReachable);
		return isReachable;
	}
	
    // Overload
	public static boolean isResponding(ManagedEntity instance) throws Exception {
		
		// get IP address
		String ipAddr = ((VirtualMachine)instance).getGuest().getIpAddress();
//		System.out.println("IP address: " + ipAddr);
		
		if(ipAddr == null) {
			System.out.println("No IP address.");
			// this is not reachable. b/c the ip is not available when power off
			// consider this as dead
			return false;
		}
		
		return isResponding(ipAddr);
	}
	
	
	
	/*public boolean isReachable(String ipAddr) {
		
		// Ping the VM
		InetAddress inetVM;
		try {
			inetVM = InetAddress.getByName(ipAddr);
			
			try {
				isReachable = inetVM.isReachable(timeout);
			} catch (IOException e) {
				System.out.println("Time out");
				isReachable = false;
			}
			
		} catch (UnknownHostException e) {
			System.out.println("Invalid IP address");
			isReachable = false;
		}
		
		System.out.println("reachable: " + isReachable);
		return isReachable;
	}*/
}
