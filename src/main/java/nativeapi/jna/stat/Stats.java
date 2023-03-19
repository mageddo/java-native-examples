package nativeapi.jna.stat;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
 * @see https://man7.org/linux/man-pages/man2/lstat.2.html
 */
public interface Stats extends Library {

  Stats INSTANCE = Native.loadLibrary(Platform.C_LIBRARY_NAME, Stats.class);

  /**
   * int stat(const char *restrict pathname, struct stat *restrict statbuf);
   * @deprecated  don't use that because it's not availble in glibc 2.24, use via syscall instead.
   * @see #wrappedStat(String, Stat)
   */
  int stat(String pathname, Stat statbuf);

  int syscall(int number, Object... args);

  default int wrappedStat(String pathname, Stat statbuf){
    return this.syscall(4, pathname, statbuf);
  }

}
