package nativeapi.jna.syscall;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.io.File;

public class HelloWorldSyscallJNA {
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary(
            (Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        int syscall(int number, Object... args);
        void printf(String format, Object... args);
    }

    public static void main(String[] args) {

        final String pathToCreate = "/tmp/tmpdir." + System.nanoTime();
        if(Platform.isLinux()){

            if(Platform.is64Bit()){
                // create a file in x64 Linux
                System.out.println(CLibrary.INSTANCE.syscall(83, pathToCreate));
                System.out.println("file created: " + new File(pathToCreate).exists());
            }else{
                // WARNING: These syscall numbers are for x86 only
                System.out.println("PID: " + CLibrary.INSTANCE.syscall(20));
                System.out.println("UID: " + CLibrary.INSTANCE.syscall(24));
                System.out.println("GID: " + CLibrary.INSTANCE.syscall(47));
                CLibrary.INSTANCE.syscall(39, pathToCreate);
            }
        }

        // cross platform
        CLibrary.INSTANCE.printf("Hello, World\n");

        for (int i = 0; i < args.length; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }
    }
}