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
			final CppMap players = new CppMap(Map.INSTANCE.getPlayers());

			for (MapItem player : players.getItems()) {
				System.out.printf("key=%d, value=%d%n", player.getPointer().getInt(MapItem.KEY), player.getPointer().getInt(MapItem.VALUE));
			}

		}
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

		private final Pointer pointer;

		public CppMap(Pointer p) {
			this.pointer = p;
		}

		public Pointer getPointer() {
			return pointer;
		}

		public Set<MapItem> getItems(){
			final Set<MapItem> items = new HashSet<>();
			getItems(items, getPointer().getPointer(0x18));
			return items;
		}

		private void getItems(Set<MapItem> items, Pointer base){
			if(base == null || items.contains(new MapItem(base))){
				return ;
			}
			if(base.getPointer(0x28) != null) { // is not the  map itself
				items.add(new MapItem(base));
			}

			getItems(items, base.getPointer(0x8));
			getItems(items, base.getPointer(0x10));
			getItems(items, base.getPointer(0x18));
		}
	}

	public static class MapItem {
		private final Pointer pointer;
		public static final int KEY = 0x20;
		public static final int VALUE = 0x24;

		public MapItem(Pointer p) {
			this.pointer = p;
		}

		public Pointer getPointer() {
			return pointer;
		}

		@Override
		public boolean equals(Object obj) {
			return this.getPointer().equals(((MapItem)obj).getPointer());
		}

		@Override
		public int hashCode() {
			return this.getPointer().hashCode();
		}

		@Override
		public String toString() {
			return String.format("{address: %x}", Pointer.nativeValue(getPointer()));
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


}
