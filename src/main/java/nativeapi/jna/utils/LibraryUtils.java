package nativeapi.jna.utils;

import java.lang.reflect.Method;
import java.util.Collections;

import com.sun.jna.Library;
import com.sun.jna.NativeLibrary;

public class LibraryUtils {
  public static void main(String[] args) {
    findFunc("stat", "c");
  }
  private static void findFunc(String funcName, final String libname) {

    try {

      Library.Handler handler = new Library.Handler(libname, Library.class, Collections.emptyMap());
      final NativeLibrary nativeLibrary = handler.getNativeLibrary();

      try {
        final Method m = NativeLibrary.class.getDeclaredMethod("getSymbolAddress", String.class);
        m.setAccessible(true);
        System.out.printf("stat1=%s%n", m.invoke(nativeLibrary, funcName));
      } catch (Throwable e) {
        System.err.println("m1 failed: " + e.getMessage());
        e.printStackTrace();
      }
      try {
        System.out.printf("stat2=%s%n", nativeLibrary.getFunction(funcName));
      } catch (Throwable e) {
        System.err.println("m2 failed: " + e.getMessage());
        e.printStackTrace();
      }
      System.out.println("----------------------------------------");
      System.out.println();
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
