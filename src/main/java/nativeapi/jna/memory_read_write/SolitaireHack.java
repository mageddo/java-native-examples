package nativeapi.jna.memory_read_write;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;

// https://pastebin.com/Vq8wfy39
public class SolitaireHack {

  final static long baseAddress = 0x10002AFA8L;
  final static int[] offsets = new int[]{0x50, 0x14};

  private static final Kernel32 kernel32 = Native.load("kernel32", Kernel32.class);
  private static final User32 user32 = Native.load("user32", User32.class);

  public static int PROCESS_VM_READ = 0x0010;
  public static int PROCESS_VM_WRITE = 0x0020;
  public static int PROCESS_VM_OPERATION = 0x0008;

  public static void main(String... args) {
    int pid = getProcessId("Solitaire");
    WinNT.HANDLE process = openProcess(PROCESS_VM_READ | PROCESS_VM_WRITE | PROCESS_VM_OPERATION, pid);

    Pointer dynAddress = findDynAddress(process, offsets, baseAddress);

    Memory scoreMem = readMemory(process, dynAddress, 4);
    int score = scoreMem.getInt(0);
    System.out.println(score);

    byte[] newScore = new byte[]{0x22, 0x22, 0x22, 0x22};
    writeMemory(process, dynAddress, newScore);
  }

  public static int getProcessId(String window) {
    IntByReference pid = new IntByReference(0);
    user32.GetWindowThreadProcessId(user32.FindWindow(null, window), pid);

    return pid.getValue();
  }

  public static WinNT.HANDLE openProcess(int permissions, int pid) {
    return kernel32.OpenProcess(permissions, true, pid);
  }

  public static Pointer findDynAddress(WinNT.HANDLE process, int[] offsets, long baseAddress) {

    Pointer pointer = Pointer.createConstant(baseAddress);

    int size = 4;
    Memory pTemp = new Memory(size);
    Pointer pointerAddress = Pointer.NULL;

    for (int i = 0; i < offsets.length; i++) {
      if (i == 0) {
        kernel32.ReadProcessMemory(process, pointer, pTemp, size, null);
      }

      pointerAddress = Pointer.createConstant(((pTemp.getInt(0) + offsets[i])));

      if (i != offsets.length - 1) {
        kernel32.ReadProcessMemory(process, pointerAddress, pTemp, size, null);
      }


    }

    return pointerAddress;
  }

  public static Memory readMemory(WinNT.HANDLE process, Pointer address, int bytesToRead) {
    IntByReference read = new IntByReference(0);
    Memory output = new Memory(bytesToRead);

    kernel32.ReadProcessMemory(process, address, output, bytesToRead, read);
    return output;
  }

  public static void writeMemory(WinNT.HANDLE process, Pointer address, byte[] data) {
    int size = data.length;
    Memory toWrite = new Memory(size);

    for (int i = 0; i < size; i++) {
      toWrite.setByte(i, data[i]);
    }

    boolean b = kernel32.WriteProcessMemory(process, address, toWrite, size, null);
  }
}
