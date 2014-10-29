package Components;

import java.util.ArrayList;

import com.vmware.vim25.VirtualMachineQuickStats;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.VirtualMachine;

public class StatisticsManager {

	public static void printStats(ArrayList<ManagedEntity> vmList) {
		
		VirtualMachineQuickStats stats;
		
		System.out.println("\n\n\n--------------------- Stats ---------------------\n");
		for(ManagedEntity vm : vmList) {
			
			stats = ((VirtualMachine)vm).getSummary().getQuickStats();
			
			System.out.println("Name: " + vm.getName());
			System.out.println("OS: " + ((VirtualMachine)vm).getSummary().getConfig().guestFullName);
			System.out.println("Over All CPU Usage: " + stats.overallCpuUsage);
			System.out.println("Guest Memory Usage: " + stats.getGuestMemoryUsage());
			System.out.println("Host Memory Usage: " + stats.getHostMemoryUsage());
			System.out.println("IP Addresses: " + ((VirtualMachine)vm).getSummary().getGuest().getIpAddress());
			System.out.println("State: " + ((VirtualMachine)vm).getGuest().guestState);
			
			System.out.println("\n--------------------------------------------------\n");
		}
	}
}
