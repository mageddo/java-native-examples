package nativeapi.jna.customlibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;

import static nativeapi.libfinder.LibraryFinder.getFullLibPath;

public interface GoCalculator extends Library {

  GoCalculator INSTANCE = Native.load(getFullLibPath("/libgocalc.so"), GoCalculator.class);

  int Add(int a, int b);
}
