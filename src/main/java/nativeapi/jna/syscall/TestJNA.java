package nativeapi.jna.syscall;

import com.sun.jna.Library;
import com.sun.jna.Native;

public class TestJNA {
    public interface CStdLib extends Library {
        int syscall(int number, Object... args);
    }

    public static void main(String[] args) {
        CStdLib c = (CStdLib)Native.loadLibrary("c", CStdLib.class);

        // WARNING: These syscall numbers are for x86 only
        System.out.println("PID: " + c.syscall(20));
        System.out.println("UID: " + c.syscall(24));
        System.out.println("GID: " + c.syscall(47));
        c.syscall(39, "/tmp/create-new-directory-here");
    }
}