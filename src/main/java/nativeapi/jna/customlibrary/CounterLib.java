package nativeapi.jna.customlibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;

import static nativeapi.jna.customlibrary.CustomLibraryJNATest.getLibPath;

public interface CounterLib extends Library {

	CounterLib INSTANCE = Native.loadLibrary(getLibPath() + "/libcounter.so", CounterLib.class);

	int incrementAndGet();
}
