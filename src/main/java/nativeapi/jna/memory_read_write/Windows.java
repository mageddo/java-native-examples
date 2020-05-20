package nativeapi.jna.memory_read_write;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinUser;

public class Windows implements OperationalSystem {

  private final User32 user32 = User32.INSTANCE;
  private final Psapi psapi = Psapi.INSTANCE;
  private final Kernel32 kernel32 = Kernel32.INSTANCE;

//  private static final User32 user32 = Native.load("user32", User32.class);

  public void listWindows(){
    this.user32.EnumWindows(new WinUser.WNDENUMPROC() {
      @Override
      public boolean callback(WinDef.HWND hWnd, Pointer data) {
        final char[] nameBuff = new char[128];
        final int read = User32.INSTANCE.GetWindowText(hWnd, nameBuff, 128);
//        System.out.println(User32.INSTANCE.GetWindowTextLength(hWnd));
        System.out.printf("handle=%s, name=%s%n",hWnd, new String(nameBuff, 0, read));
        return true;
      }
    }, Pointer.NULL);
  }

  /**
   *
   * https://docs.microsoft.com/en-us/windows/win32/psapi/enumerating-all-processes
   */
  public void listProcesses(){
    final int[] process = new int[1024];
    final int[] dummy = new int[process.length];
    if(!this.psapi.EnumProcesses(process, process.length, dummy)){
      throw new IllegalStateException("Couldn't list processes: " + Native.getLastError());
    }

    for (int pid : process) {
      if(pid == 0){
        continue;
      }
      final String processName = this.getProcessNameById(pid);
      System.out.printf("pid=%d, name=%s%n", pid, processName);
    }
  }

  /**
   * https://docs.microsoft.com/en-us/windows/win32/api/psapi/nf-psapi-getprocessimagefilenamew
   */
  private String getProcessNameById(int pid) {
//    pid = kernel32.GetCurrentProcessId();
    final WinNT.HANDLE processHandle = kernel32.OpenProcess(WinNT.PROCESS_QUERY_INFORMATION, true, pid);
    if(processHandle == null){
      System.out.printf("WARN, status=couldn't-get-handle, pid=%d, error=%d%n", pid, Native.getLastError());
      return null;
    }
    final char[] processFileName = new char[255];
    final int read = this.psapi.GetProcessImageFileNameW(
        processHandle.getPointer(),
        processFileName,
        processFileName.length
    );
    if(read == 0){
      System.out.println("Couldn't read process name, pid=" + pid);
//      throw new IllegalStateException("Couldn't read process name, pid=" + pid);
    }
    return new String(processFileName, 0, read);
  }
}
