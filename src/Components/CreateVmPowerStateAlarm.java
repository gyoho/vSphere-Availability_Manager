 /**
   * Responsibility: create VmPowerStateAlarm for VM
   *
   * @param instance: vHost, VM
   * @return none: use try/catch to catch exception
   *
   */


package Components;

import java.rmi.RemoteException;

import com.vmware.vim25.Action;
import com.vmware.vim25.AlarmAction;
import com.vmware.vim25.AlarmExpression;
import com.vmware.vim25.AlarmSetting;
import com.vmware.vim25.AlarmSpec;
import com.vmware.vim25.AlarmTriggeringAction;
import com.vmware.vim25.DuplicateName;
import com.vmware.vim25.InvalidName;
import com.vmware.vim25.ManagedObjectReference;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.SendEmailAction;
import com.vmware.vim25.StateAlarmExpression;
import com.vmware.vim25.StateAlarmOperator;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServiceInstance;

public class CreateVmPowerStateAlarm {
	
	public static void registerAlarm(ServiceInstance serviceInstance, ManagedEntity mgent) throws InvalidName, DuplicateName, RuntimeFault, RemoteException {
		
		AlarmManager alarmMgr = serviceInstance.getAlarmManager();
		if(!isAlarmRegistered(alarmMgr, mgent)) {
			createNewAlarm(alarmMgr, mgent);
		}		
	}
	
	public static boolean isAlarmRegistered(AlarmManager alarmMgr, ManagedEntity mgent) throws InvalidName, DuplicateName, RuntimeFault, RemoteException {
		
		// get all registered alarms info for this instance
		Alarm[] alarms = alarmMgr.getAlarm(mgent);
		
		// check if the specified alarm is registered
		for(Alarm alarm : alarms) {
			if(alarm.getAlarmInfo().getName().equals("VmPowerStateAlarm")) {
				return true;
			}
		}
		
		return false;
	}
	
	private static void createNewAlarm(AlarmManager alarmMgr, ManagedEntity mgent) throws InvalidName, DuplicateName, RuntimeFault, RemoteException {
		
		// Action to perform when the alarm is triggered
		AlarmAction emailAction = createAlarmTriggerAction(createEmailAction());
		// Top-level alarm expression that defines trigger conditions
		AlarmExpression expression = createAlarmExpression();
		// Tolerance and maximum frequency settings
		AlarmSetting alarmset = createAlarmSetting();

		AlarmSpec spec = new AlarmSpec();
		spec.setAction(emailAction);
		spec.setExpression(expression);
		spec.setName("VmPowerStateAlarm");
		spec.setDescription("Monitor VM state and send notification trap if VM powers off");
		spec.setEnabled(true);
		spec.setSetting(alarmset);
		
		alarmMgr.createAlarm(mgent, spec);
	}

	private static SendEmailAction createEmailAction() {
	    SendEmailAction action = new SendEmailAction();
	    action.setToList("yaopeng.gyoho.wu@gmail.com");
	    action.setCcList("test@test.com");
	    action.setSubject("Alarm - VM Powered Off\n");
	    action.setBody("Description:{eventDescription}\n"
	        + "TriggeringSummary:{triggeringSummary}\n"
	        + "newStatus:{newStatus}\n"
	        + "oldStatus:{oldStatus}\n"
	        + "target:{target}");
	    return action;
	}

	private static AlarmExpression createAlarmExpression() {
		StateAlarmExpression expression = new StateAlarmExpression();
	    expression.setType("VirtualMachine");
	    expression.setStatePath("runtime.powerState");
	    expression.setOperator(StateAlarmOperator.isEqual);
	    expression.setYellow("poweredOff");
	    return expression;
	}

	private static AlarmTriggeringAction createAlarmTriggerAction(Action action) {
		AlarmTriggeringAction alarmAction = new AlarmTriggeringAction();
	    alarmAction.setYellow2red(true);
	    alarmAction.setAction(action);
	    return alarmAction;
	}

	private static AlarmSetting createAlarmSetting() {
		AlarmSetting alarmset = new AlarmSetting();
		alarmset.setReportingFrequency(0);
		alarmset.setToleranceRange(0);
		return alarmset;
	}
}
