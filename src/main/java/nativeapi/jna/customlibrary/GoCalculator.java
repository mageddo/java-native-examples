package nativeapi.jna.customlibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;

import static nativeapi.jna.customlibrary.CustomLibraryJNATest.getLibPath;

public interface GoCalculator extends Library {
	GoCalculator INSTANCE = Native.loadLibrary(getLibPath() + "/libgocalc.so", GoCalculator.class);
	int Add(int a, int b);
}
