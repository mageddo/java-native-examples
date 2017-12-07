package nativeapi.jna.windows;

import java.util.*;
import com.sun.jna.*;
//import com.sun.jna.win32.*;

public class Main {

	public static void main(String args[]){
		SystemPowerStatus sts = new SystemPowerStatus();
		System.out.println(Kernel32.INSTANCE.GetSystemPowerStatus(sts));
		System.out.println(sts);
	}

	public interface Kernel32 extends 
//		StdCallLibrary
		Library
	 {
		Kernel32 INSTANCE = (Kernel32) Native.loadLibrary("Kernel32", Kernel32.class);
		int GetSystemPowerStatus(SystemPowerStatus result);
	}

	public static class SystemPowerStatusPointer 
		extends SystemPowerStatus 
		implements Structure.ByReference {
	}

	/**
	 * @see http://msdn2.microsoft.com/en-us/library/aa373232.aspx
	 */
	public static class SystemPowerStatus extends Structure {
		public byte ACLineStatus;
		public byte BatteryFlag;
		public byte BatteryLifePercent;
		public byte Reserved1;
		public int BatteryLifeTime;
		public int BatteryFullLifeTime;

		@Override
		protected List<String> getFieldOrder() {
			ArrayList<String> fields = new ArrayList<String>();
			fields.add("ACLineStatus");
			fields.add("BatteryFlag");
			fields.add("BatteryLifePercent");
			fields.add("Reserved1");
			fields.add("BatteryLifeTime");
			fields.add("BatteryFullLifeTime");
			return fields;
		}

		/**
		 * The AC power status
		 */
		public String getACLineStatusString() {
			switch (ACLineStatus) {
				case (0): return "Offline";
				case (1): return "Online";
				default: return "Unknown";
			}
		}

		/**
		 * The battery charge status
		 */
		public String getBatteryFlagString() {
			switch (BatteryFlag) {
				case (1): return "High, more than 66 percent";
				case (2): return "Low, less than 33 percent";
				case (4): return "Critical, less than five percent";
				case (8): return "Charging";
				case ((byte) 128): return "No system battery";
				default: return "Unknown";
			}
		}

		/**
		 * The percentage of full battery charge remaining
		 */
		public String getBatteryLifePercent() {
			return (BatteryLifePercent == (byte) 255) ? "Unknown" : BatteryLifePercent + "%";
		}

		/**
		 * The number of seconds of battery life remaining
		 */
		public String getBatteryLifeTime() {
			return (BatteryLifeTime == -1) ? "Unknown" : BatteryLifeTime + " seconds";
		}

		/**
		 * The number of seconds of battery life when at full charge
		 */
		public String getBatteryFullLifeTime() {
			return (BatteryFullLifeTime == -1) ? "Unknown" : BatteryFullLifeTime + " seconds";
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("ACLineStatus: " + getACLineStatusString() + "\n");
			sb.append("Battery Flag: " + getBatteryFlagString() + "\n");
			sb.append("Battery Life: " + getBatteryLifePercent() + "\n");
			sb.append("Battery Left: " + getBatteryLifeTime() + "\n");
			sb.append("Battery Full: " + getBatteryFullLifeTime() + "\n");
			return sb.toString();
		}
	}
}