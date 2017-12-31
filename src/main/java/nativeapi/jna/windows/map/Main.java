package nativeapi.jna.windows.map;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		System.out.println("hexpid=" + Integer.toHexString(new Integer(ManagementFactory.getRuntimeMXBean().getName().split("@")[0])));
		final Scanner in = new Scanner(System.in);
		for(;;){
			System.out.println(Map.INSTANCE.playerAction(in.nextInt()));
			System.out.printf("players=%s\n", Map.INSTANCE.getPlayers());

			final Pointer first = Map.INSTANCE.getPlayers().getPointer(0x18);
			Set<Pointer> items = new HashSet<>();
			getAllItems(items, first);

			System.out.println("map content:");
			for (Pointer item : items) {
				System.out.printf("> item=%s %s=%s, %d=%d%n", item, item.getPointer(0x20), item.getPointer(0x24), item.getInt(0x20), item.getInt(0x24));
			}
		}

	}

	public static void getAllItems(Set<Pointer> items, Pointer base){
		if(base == null || items.contains(base)){
			return ;
		}
		items.add(base);
		getAllItems(items, base.getPointer(0x8));
		getAllItems(items, base.getPointer(0x10));
		getAllItems(items, base.getPointer(0x18));
	}


	public interface Map extends Library{
		Map INSTANCE = Native.loadLibrary("D:/dev/projects/java-native-examples/src/main/resources/lib/libmap.dll", Map.class);
		Pointer getPlayers();

		/**
		 * 1 = Reduce Player x Health if doesn't exists create a new player with 100  health
		 * @param action
		 */
		int playerAction(int action);
	}

	public static class CppMap {
		private final Pointer p;

		public CppMap(Pointer p) {
			this.p = p;
		}

		public void iterate(){
			final Pointer first = p.getPointer(0x18);
		}
	}

	public static class CMap extends Structure {
		public int f1;
		public int f2;
		public int f3;
		public int f4;
		public Pointer f5;
		public Pointer first;
		public Pointer last;
		@Override
		protected List<String> getFieldOrder() {
			return Arrays.asList("f1", "f2", "f3", "f4", "f5", "first", "last");
		}

	}

	public static class MapItem {

		private final Pointer pointer;

		public MapItem(Pointer p) {
			this.pointer = p;
		}
		public Pointer getKey(){
			return getPointer().getPointer(0x20);
		}
		public Pointer getValue(){
			return getPointer().getPointer(0x24);
		}

		public Pointer getPointer() {
			return pointer;
		}
	}
}
