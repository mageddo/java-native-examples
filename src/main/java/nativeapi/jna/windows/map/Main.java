package nativeapi.jna.windows.map;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class Main {
	public static void main(String[] args) {
		final Pointer players = Map.INSTANCE.getPlayers();
		System.out.printf("players=%s, first=%s%n", players, players.getInt(0));
	}

	public interface Map extends Library{
		Map INSTANCE = Native.loadLibrary("D:/dev/projects/java-native-examples/src/main/resources/lib/libmap.dll", Map.class);
		Pointer getPlayers();
	}
}
