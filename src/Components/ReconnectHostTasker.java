 /**
   * Responsibility: reconnect the vHost to the vCenter
   *
   * @param none
   * @return none
   * 
   * Implementation: sleeps for 2 minutes
   * 
   */


package Components;

public class ReconnectHostTasker {
	
	public static void reconnect() throws InterruptedException {

		// Takes long time to get the host connected
		// after rebooting it from the mgmt center
		// so wait for a while
		Thread.sleep(120*1000);
		
		
		// TODO: if there is a better way...
	}

}
