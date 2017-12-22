package nativeapi.jna.loadingmap;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public class Main {
	interface MyMapLib extends Library {
		MyMapLib INSTANCE = Native.loadLibrary("/home/elvis/dev/projects/java-native-examples/src/main/c/map.so", MyMapLib.class);

		Pointer findUserById();
	}

	public static void main(String[] args) {
		Pointer result = MyMapLib.INSTANCE.findUserById();
		System.out.println(result);
	}
}
