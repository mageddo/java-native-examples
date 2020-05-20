package nativeapi.jna.customlibrary;

import com.sun.jna.Library;
import com.sun.jna.Native;

import static nativeapi.libfinder.LibraryFinder.getFullLibPath;

public interface CounterLib extends Library {

  CounterLib INSTANCE = Native.load(getFullLibPath("/libcounter.so"), CounterLib.class);

  int incrementAndGet();
}
