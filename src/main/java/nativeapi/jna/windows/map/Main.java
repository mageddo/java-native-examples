package nativeapi.jna.windows.map;

import com.sun.jna.*;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		System.out.println("hexpid=" + Integer.toHexString(new Integer(ManagementFactory.getRuntimeMXBean().getName().split("@")[0])));
		final Scanner in = new Scanner(System.in);
		final CppMap players = new CppMap(Map.INSTANCE.getPlayers());

		for(;;){
			final String input = in.next().toUpperCase();
			switch (input){
				case "ADD":
					System.out.println("type an integer key:");
					final int key = in.nextInt();

					System.out.println("type an integer value:");
					final int value = in.nextInt();

//					final Memory keyp = new Memory(Native.LONG_SIZE);
//					keyp.setInt(0, key);
//					final Memory valuep = new Memory(Native.LONG_SIZE);
//					valuep.setInt(0, value);
					Map.INSTANCE.put(players.getPointer(), key, value);

					break;

				default:
					System.out.println(Map.INSTANCE.playerAction(Integer.parseInt(input)));
			}
			System.out.printf("players=%s, size=%d%n", players, players.size());
			for (MapItem player : players.getItems()) {
				System.out.printf(
					"c0=%d, key=%d, value=%d, value2=%d%n",
					player.getPointer().getInt(0),
					player.getPointer().getInt(MapItem.KEY),
					player.getPointer().getInt(MapItem.VALUE),
					player.getPointer().getInt(0x28)
				);
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

		Pointer get(Pointer map, Pointer key);
		int put(Pointer map, Object key, Object value);
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

		public int size(){
			return getPointer().getInt(0x28);
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

		@Override
		public String toString() {
			return getPointer().toString();
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
