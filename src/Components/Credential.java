package Components;

public class Credential {
	
	private String managementCenterIpAddr = "130.65.132.14";
	private String vCenterIpAddr = "130.65.132.106";
	private String resourcePoolName = "Team06_vHOSTS";
	private String usrName = "administrator";
	private String passwd = "12!@qwQW";
	// if user powers it off, it takes time to reflect the change
	private int timeout = 7*1000;
	
	public String getManagementCenterIpAddr() {
		return managementCenterIpAddr;
	}
	
	public String getvCenterIpAddr() {
		return vCenterIpAddr;
	}
	
	public String getResourcePoolName() {
		return resourcePoolName;
	}
	
	public String getUsrName() {
		return usrName;
	}
	
	public String getPasswd() {
		return passwd;
	}
	
	public int getTimeout() {
		return timeout;
	}
}
