package nativeapi.jna.memory_read_write;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Psapi extends Library {

  Psapi INSTANCE = Native.load("Psapi", Psapi.class);

  boolean EnumProcesses(int[] pProcessIds, int cb, int[] pBytesReturned);

  int GetProcessImageFileNameW(Pointer hProcess, char[] lpImageFileName, int nSize);

}
