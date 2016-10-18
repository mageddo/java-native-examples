package nativeapi.jna.customlibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;

import static nativeapi.jna.customlibrary.CustomLibraryJNATest.LibHello.INSTANCE;

/**
 * Created by elvis on 17/10/16.
 */
public class CustomLibraryJNATest {

	static {
//		System.setProperty("jna.library.path", Class.class.getResource("/lib").getPath());
	}

	interface LibHello extends Library {

		LibHello INSTANCE = (LibHello) Native.loadLibrary(getLibPath() + "/libhello.so", LibHello.class);

		void hello(String message);

	}

	public static String getLibPath() {
		return Class.class.getResource("/lib").getPath();
	}

	public static void main(String[] args) {

		INSTANCE.hello("Elvis!");

	}
}
