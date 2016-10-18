package nativeapi.jna.syscall;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public class HelloWorldJNA {
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary(
            (Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        int syscall(int number, Object... args);
        void printf(String format, Object... args);
    }

    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("Hello, World\n");

        CLibrary.INSTANCE.syscall(83, "/tmp/elvisx2");


        for (int i = 0; i < args.length; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }
}